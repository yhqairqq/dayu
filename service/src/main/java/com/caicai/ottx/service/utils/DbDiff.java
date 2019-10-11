package com.caicai.ottx.service.utils;

import com.alibaba.fastsql.sql.ast.SQLDataType;
import com.alibaba.fastsql.sql.ast.SQLDataTypeImpl;
import com.alibaba.fastsql.sql.ast.SQLExpr;
import com.alibaba.fastsql.sql.ast.SQLStatement;
import com.alibaba.fastsql.sql.ast.expr.*;
import com.alibaba.fastsql.sql.ast.statement.*;
import com.alibaba.fastsql.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.fastsql.sql.dialect.mysql.ast.MySqlUnique;
import com.alibaba.fastsql.sql.repository.SchemaObject;
import com.alibaba.fastsql.sql.repository.SchemaRepository;
import com.alibaba.fastsql.util.JdbcConstants;
import com.alibaba.fastsql.util.StringUtils;
import com.alibaba.otter.canal.parse.driver.mysql.packets.server.FieldPacket;
import com.alibaba.otter.canal.parse.driver.mysql.packets.server.ResultSetPacket;
import com.alibaba.otter.canal.parse.inbound.TableMeta;
import com.alibaba.otter.canal.parse.inbound.mysql.MysqlConnection;
import com.alibaba.otter.canal.parse.inbound.mysql.ddl.DruidDdlParser;
import com.alibaba.otter.manager.biz.common.DataSourceCreator;
import com.alibaba.otter.shared.common.model.config.data.DataMedia;
import com.alibaba.otter.shared.common.model.config.data.DataMediaPair;
import com.alibaba.otter.shared.common.model.config.data.db.DbMediaSource;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.alibaba.otter.shared.common.utils.thread.NamedThreadFactory;
import com.alibaba.otter.shared.compare.model.IndexTable;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by huaseng on 2019/8/6.
 */
@Component
public class DbDiff {

    private Logger logger = LoggerFactory.getLogger(DbDiff.class);
    private ExecutorService executorService = Executors.newCachedThreadPool(new NamedThreadFactory("DbDiff"));
    private Map<Long, Map<String, IndexTable>> indexTables;
    private static final String ESCAPE = "`";
    private DataSourceCreator dataSourceCreator;

    LoadingCache<DataMedia, MysqlConnection> mysqlConnCache;
    private LoadingCache<DataMedia, JdbcTemplate> jdbcTemplateCache;
    private CompletionService complationService;
    private ExecutorService executor;
    public DbDiff() {

        int fixSize = (int)(Runtime.getRuntime().availableProcessors()*2*0.8);
        executor = Executors.newFixedThreadPool(fixSize);
        complationService = new ExecutorCompletionService(executor);

        mysqlConnCache = CacheBuilder.newBuilder().
                build(new CacheLoader<DataMedia, MysqlConnection>() {
                    @Override
                    public MysqlConnection load(DataMedia key) throws Exception {
                        DbMediaSource dbMediaSource = ((DbMediaSource) key.getSource());
                        String url = ((DbMediaSource) key.getSource()).getUrl();
                        String host = url.substring(url.lastIndexOf("/") + 1);
                        String[] arr = host.split(":");
                        InetAddress inetAddress = InetAddress.getByName(arr[0]);
                        InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, Integer.parseInt(arr[1]));
                        return new MysqlConnection(inetSocketAddress, dbMediaSource.getUsername(), dbMediaSource.getPassword());
                    }
                });

        jdbcTemplateCache = CacheBuilder.newBuilder().build(new CacheLoader<DataMedia, JdbcTemplate>() {
            @Override
            public JdbcTemplate load(DataMedia key) throws Exception {
                DataSource dataSource = dataSourceCreator.createDataSource(key.getSource());
                return new JdbcTemplate(dataSource);
            }
        });
    }

    /**
     * 生成计算地图
     */
    public void compare(Pipeline pipeline) throws Exception {
        List<DataMediaPair> dataMediaPairs = pipeline.getPairs();
        List<Future> resultList = new ArrayList<>();
        for (DataMediaPair dataMediaPair : dataMediaPairs) {
          Exception e =   worker(dataMediaPair);
          if(e != null){
              throw worker(dataMediaPair);
          }
        }
    }

    private Exception worker(DataMediaPair dataMediaPair) {
        DataMedia source = dataMediaPair.getSource();
        DataMedia target = dataMediaPair.getTarget();
        String schema = source.getNamespace();
        String targetSchema = target.getNamespace();
        Exception result = null;
        DataMedia.ModeValue modeValue = source.getNameMode();
        DataMedia.ModeValue targetModelValue = target.getNameMode();
        try {
            if (modeValue.getMode().isSingle()) {
                String table = modeValue.getSingleValue();
                String targetTable = null;
                if (targetModelValue.getMode().isWildCard()) {
                    targetTable = table;
                } else {
                    targetTable = targetModelValue.getSingleValue();
                }
                result = compareRecord(schema, table, source, targetSchema, targetTable, target);

                //比较指定列数据
            } else if (modeValue.getMode().isMulti()) {
                List<String> tables = modeValue.getMultiValue();
                for (String table : tables) {
                    if (targetModelValue.getMode().isWildCard()) {
                        String targetTable = table;
                        result =  compareRecord(schema, table, source, targetSchema, targetTable, target);
                        //比较指定列数据
                    } else {
                    }
                }
            }
        } catch (Exception e) {

        }
        if(result == null){
            DayCounter counter = new DayCounter();
            logger.info("table [%s %s] and table[ %s %s] between %s and %s",dataMediaPair.getSource().getNamespace(),dataMediaPair.getSource().getName(),dataMediaPair.getTarget().getNamespace(),dataMediaPair.getTarget().getName(),counter.getDate(),counter.addDay(1).getDate());
        }
        return result;
    }

    public void preCompare(String schema, String table, DataMedia source) throws Exception {
        IndexTable indexTable = findIndexTable(source, schema, table);
        if (indexTable == null) {
            throw new Exception("indexTable is null");
        }
        String timeColumn = null;
        String primaryKey = null;
        // update_time的索引
        if (indexTable.isIndexField("update_time")) {
            timeColumn = "update_time";
        } else if (indexTable.isIndexField("create_time")) {
            timeColumn = "create_time";
        }
        if (timeColumn == null) {
            throw new Exception(String.format("no time index in %s %s", schema, table));
        }
        //获取主键名称
        primaryKey = indexTable.getPrimaryKeyFieldName();
        if (primaryKey == null) {
            throw new Exception(String.format("no primary in %s %s", schema, table));
        }
    }


    public TableMeta getTableMeta(DataMedia source, String schema, String table) throws IOException {

        MysqlConnection mysqlConnection = null;
        try {
            mysqlConnection = mysqlConnCache.get(source);
            mysqlConnection.connect();
            ResultSetPacket packet = mysqlConnection.query("show create table  " + getFullName(schema, table));
            TableMeta tableMeta = null;
            SchemaRepository repository = new SchemaRepository(JdbcConstants.MYSQL);
            if (packet.getFieldValues().size() > 1) {
                String createDDL = packet.getFieldValues().get(1);
                repository.setDefaultSchema(schema);
                repository.console(createDDL);
                SchemaObject data = repository.findSchema(schema).findTable(table);
                if (data == null) {
                    return null;
                }
                SQLStatement statement = data.getStatement();
                if (statement == null) {
                    return null;
                }
                if (statement instanceof SQLCreateTableStatement) {
                    tableMeta = parse((SQLCreateTableStatement) statement);
                }
            }
            return tableMeta;

        } catch (Exception e) {
            e.printStackTrace();
            mysqlConnection.disconnect();
        } finally {
            mysqlConnection.disconnect();
        }
        return null;
    }

    private TableMeta parse(SQLCreateTableStatement statement) {
        int size = statement.getTableElementList().size();
        if (size > 0) {
            TableMeta tableMeta = new TableMeta();
            for (int i = 0; i < size; ++i) {
                SQLTableElement element = statement.getTableElementList().get(i);
                processTableElement(element, tableMeta);
            }
            return tableMeta;
        }

        return null;
    }

    private void processTableElement(SQLTableElement element, TableMeta tableMeta) {
        if (element instanceof SQLColumnDefinition) {
            TableMeta.FieldMeta fieldMeta = new TableMeta.FieldMeta();
            SQLColumnDefinition column = (SQLColumnDefinition) element;
            String name = getSqlName(column.getName());
            // String charset = getSqlName(column.getCharsetExpr());
            SQLDataType dataType = column.getDataType();
            String dataTypStr = dataType.getName();
            if (dataType.getArguments().size() > 0) {
                dataTypStr += "(";
                for (int i = 0; i < column.getDataType().getArguments().size(); i++) {
                    if (i != 0) {
                        dataTypStr += ",";
                    }
                    SQLExpr arg = column.getDataType().getArguments().get(i);
                    dataTypStr += arg.toString();
                }
                dataTypStr += ")";
            }

            if (dataType instanceof SQLDataTypeImpl) {
                SQLDataTypeImpl dataTypeImpl = (SQLDataTypeImpl) dataType;
                if (dataTypeImpl.isUnsigned()) {
                    dataTypStr += " unsigned";
                }

                if (dataTypeImpl.isZerofill()) {
                    dataTypStr += " zerofill";
                }
            }

            if (column.getDefaultExpr() == null || column.getDefaultExpr() instanceof SQLNullExpr) {
                fieldMeta.setDefaultValue(null);
            } else {
                fieldMeta.setDefaultValue(DruidDdlParser.unescapeQuotaName(getSqlName(column.getDefaultExpr())));
            }

            fieldMeta.setColumnName(name);
            fieldMeta.setColumnType(dataTypStr);
            fieldMeta.setNullable(true);
            List<SQLColumnConstraint> constraints = column.getConstraints();
            for (SQLColumnConstraint constraint : constraints) {
                if (constraint instanceof SQLNotNullConstraint) {
                    fieldMeta.setNullable(false);
                } else if (constraint instanceof SQLNullConstraint) {
                    fieldMeta.setNullable(true);
                } else if (constraint instanceof SQLColumnPrimaryKey) {
                    fieldMeta.setKey(true);
                    fieldMeta.setNullable(false);
                } else if (constraint instanceof SQLColumnUniqueKey) {
                    fieldMeta.setUnique(true);
                }
            }
            tableMeta.addFieldMeta(fieldMeta);
        } else if (element instanceof MySqlPrimaryKey) {
            MySqlPrimaryKey column = (MySqlPrimaryKey) element;
            List<SQLSelectOrderByItem> pks = column.getColumns();
            for (SQLSelectOrderByItem pk : pks) {
                String name = getSqlName(pk.getExpr());
                TableMeta.FieldMeta field = tableMeta.getFieldMetaByName(name);
                field.setKey(true);
                field.setNullable(false);
            }
        } else if (element instanceof MySqlUnique) {
            MySqlUnique column = (MySqlUnique) element;
            List<SQLSelectOrderByItem> uks = column.getColumns();
            for (SQLSelectOrderByItem uk : uks) {
                String name = getSqlName(uk.getExpr());
                TableMeta.FieldMeta field = tableMeta.getFieldMetaByName(name);
                field.setUnique(true);
            }
        }
    }

    private String getSqlName(SQLExpr sqlName) {
        if (sqlName == null) {
            return null;
        }

        if (sqlName instanceof SQLPropertyExpr) {
            SQLIdentifierExpr owner = (SQLIdentifierExpr) ((SQLPropertyExpr) sqlName).getOwner();
            return DruidDdlParser.unescapeName(owner.getName()) + "."
                    + DruidDdlParser.unescapeName(((SQLPropertyExpr) sqlName).getName());
        } else if (sqlName instanceof SQLIdentifierExpr) {
            return DruidDdlParser.unescapeName(((SQLIdentifierExpr) sqlName).getName());
        } else if (sqlName instanceof SQLCharExpr) {
            return ((SQLCharExpr) sqlName).getText();
        } else if (sqlName instanceof SQLMethodInvokeExpr) {
            return DruidDdlParser.unescapeName(((SQLMethodInvokeExpr) sqlName).getMethodName());
        } else {
            return sqlName.toString();
        }
    }

    public Exception compareRecord(String sschema, String stable, DataMedia source,
                                   String targetSchema, String targetTable, DataMedia target
    ) {
        Exception result = null;
        try {
            //比较检查
            preCompare(sschema, stable, source);
            preCompare(targetSchema, stable, target);
            int scount = selectCount(source, sschema, stable);
            int tcount = selectCount(target, targetSchema, targetTable);
            int currentId = 0;
            int pageSize = 10;
            int limit = pageSize;
            if (scount != tcount) {
                return new Exception(String.format("table[%s] records not same with table[%s]", getFullName(sschema, stable), getFullName(targetSchema, targetTable)));
            }
            if(scount == 0){
                logger.warn("no data");
                return null;
            }
            TableMeta sTableMeta = getTableMeta(source, sschema, stable);
            TableMeta tTableMeta = getTableMeta(target, targetSchema, targetTable);
            IndexTable indexTable = findIndexTable(source,sschema,stable);
            String primaryKey = indexTable.getPrimaryKeyFieldName();
            List<String> fields = new ArrayList<>();
            List<String> defaultFields = Arrays.asList(indexTable.getPrimaryKeyFieldName(),"create_time");
            List<String> extraFields = Arrays.asList("name","city_id");
            for(TableMeta.FieldMeta fieldMeta:sTableMeta.getFields()){
                if(defaultFields.contains(fieldMeta.getColumnName()) || extraFields.contains(fieldMeta.getColumnName())){
                    fields.add(fieldMeta.getColumnName());
                }
            }
            int page = 0;
            List<Future<Exception>> resultList = new ArrayList<>();
            while(true){
                List<List<String>> sData = selectList(source, sschema, stable, fields, primaryKey, currentId,limit);
                List<List<String>> tData = selectList(target, targetSchema, targetTable,fields,primaryKey,currentId,limit);
//                Exception e =   compare(sData, tData, fields);
                resultList.add(complationService.submit(new Callable() {
                    @Override
                    public Exception call() throws Exception {
                        return compare(sData, tData, fields);
                    }
                }));
//                if(e != null){
//                    e.printStackTrace();
//                    return e;
//                }
                if(limit > sData.size() || limit > tData.size()){
                    return result;
                }
                List<String> values = sData.get(sData.size()-1);
                int index = -1;
                for(int i = 0;i < fields.size();i++){
                    if(fields.get(i).equals(primaryKey)){
                        index = i;
                        break;
                    }
                }
                if(index == -1){
                    throw new Exception(String.format("[%s %s not pks]",sschema,stable));
                }
                currentId = Integer.parseInt(values.get(index));
                page++;
                if((scount - page*pageSize) < pageSize){
                    limit = scount - page*pageSize;
                }
                if(page*pageSize > scount){
                    break;
                }
            }
            if(!CollectionUtils.isEmpty(resultList)){
               for(Future<Exception> exceptionFuture:resultList){
                   if(exceptionFuture.get() != null){
                       return exceptionFuture.get();
                   }
               }
            }


        } catch (Exception e) {
            e.printStackTrace();
            result = e;
        }
        return result;

    }

    private Exception compare(List<List<String>> sData, List<List<String>> tData, List<String> fields) throws Exception {
        if(sData.size() != tData.size()){
            return new Exception("数据量不一致,[]sData size is " + sData.size() +" and tData size is " + tData.size());
        }
        for(int i=0;i<sData.size();i++){
            List<String> values = sData.get(i);
            for(int j=0;j<values.size();j++){
                if(!StringUtils.equalsIgnoreCase(values.get(j),tData.get(i).get(j))){
                    return new Exception(String.format(" [fields %s %s not equal  %s ]", fields.toString(),sData.get(i).toString(), tData.get(i).toString()));
                }
            }
        }
        return null;
    }
    class DayCounter{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar now = Calendar.getInstance();
        public String getDate(){
            return format.format(now.getTime());
        }
        public DayCounter addDay(int days){
            now.add(Calendar.DAY_OF_YEAR, days);
            return this;
        }
    }

    private List<List<String>> selectList(DataMedia dataMedia, String schema, String table, List<String> fields, String primaryKey, int privateValue, int limit) throws ExecutionException {
        JdbcTemplate jdbcTemplate = jdbcTemplateCache.get(dataMedia);
        //查询当天的记录
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar now = Calendar.getInstance();
        Date today = now.getTime();
        String startTime = format.format(today);
        now.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = now.getTime();
        String endTime = format.format(tomorrow);
        //getSql
        String sql = getSql(primaryKey,schema,table,"create_time",fields);
        return jdbcTemplate.query(sql, new Object[]{startTime,endTime,privateValue,limit},new ResultSetExtractor<List<List<String>>>() {
            @Override
            public List<List<String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<List<String>> data = new ArrayList<>();
                while (rs.next()) {
                    List<String> values = new ArrayList<>();
                    for (String field : fields) {
                        values.add(rs.getString(field));
                    }
                    data.add(values);
                }
                return data;
            }
        });
    }

    private int selectCount(DataMedia source, String schema, String table) throws Exception {
//        DataSource dataSource = dataSourceCreator.createDataSource(source.getSource());
        JdbcTemplate jdbcTemplate = jdbcTemplateCache.get(source);
//                new JdbcTemplate(dataSource);
        IndexTable indexTable = findIndexTable(source, schema, table);
        if (indexTable == null) {
            throw new Exception("indexTable is null");
        }
        String timeColumn = null;
        String primaryKey = null;
        // update_time的索引
        if (indexTable.isIndexField("update_time")) {
            timeColumn = "update_time";
        } else if (indexTable.isIndexField("create_time")) {
            timeColumn = "create_time";
        }
        //获取主键名称
        primaryKey = indexTable.getPrimaryKeyFieldName();
        //查询当天的记录
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar now = Calendar.getInstance();
        Date today = now.getTime();
        String startTime = format.format(today);
        now.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = now.getTime();
        String endTime = format.format(tomorrow);

        String sql = getSql(primaryKey, timeColumn, schema, table);
        return jdbcTemplate.query(sql, new Object[]{startTime, endTime}, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                if(rs.next()){
                    return rs.getInt("count");
                }
                return 0;
            }
        });
    }

    private String appendEscape(String columnName) {
        return ESCAPE + columnName + ESCAPE;
    }

    private String getSql(String primaryKeyField, String timeFields, String schema, String table) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" count(").append(appendEscape(primaryKeyField)).append(") ").append(" as ").append(" count ");
        sql.append(" from ").append(getFullName(schema, table)).append(" where (");
        sql.append(appendEscape(timeFields)).append(" between ").append("? ").append("and ").append("? ").append(" )");
        return sql.toString().intern();
    }

    private String getSql(String primaryKey,String schema, String table,String timeSplit,List<String> fields) {
        StringBuilder sql = new StringBuilder("select ");
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            sql.append(appendEscape(fields.get(i))).append((i + 1 < size) ? " , " : "");
        }
        sql.append(" from ").append(getFullName(schema, table)).append(" where ( ");
        sql.append(appendEscape(timeSplit)).append(" between ").append("? ").append("and ").append("? ");
        sql.append(" and ").append(appendEscape(primaryKey)).append(" > ?").append(" )").append(" limit ?");
        return sql.toString().intern();
    }

    private IndexTable findIndexTable(DataMedia source, String schema, String table) throws IOException {
        MysqlConnection mysqlConnection = null;
        try {
            mysqlConnection = mysqlConnCache.get(source);
            mysqlConnection.connect();
            ResultSetPacket packet = mysqlConnection.query("show index from " + getFullName(schema, table));
            return parseTableMetaByDesc(packet, schema, table);
        } catch (Exception e) {
            e.printStackTrace();
            mysqlConnection.disconnect();
        } finally {
            mysqlConnection.disconnect();
        }
        return null;

    }

    private IndexTable parseTableMetaByDesc(ResultSetPacket packet, String schema, String table) {
        IndexTable indexTable = new IndexTable();
        int cols = packet.getFieldDescriptors().size();
        List<String> columns = new ArrayList<>();
        for (FieldPacket fieldPacket : packet.getFieldDescriptors()) {
            columns.add(fieldPacket.getName());
        }
        int index = 0;
        List<String> values = null;
        List<List<String>> data = new ArrayList<>();
        for (String value : packet.getFieldValues()) {
            if (index % cols == 0) {
                values = new ArrayList<>();
                data.add(values);
            }
            values.add(value);
            index++;

        }
        indexTable.setColumns(columns);
        indexTable.setDatas(data);
        indexTable.setSchema(schema);
        indexTable.setTable(table);
        return indexTable;
    }


    private String getFullName(String schema, String table) {
        StringBuilder builder = new StringBuilder();
        return builder.append('`')
                .append(schema)
                .append('`')
                .append('.')
                .append('`')
                .append(table)
                .append('`')
                .toString();
    }


    public static void main(String[] args) throws IOException {
        InetAddress inetAddress = InetAddress.getByName("rm-bp10p2vl5x3c4mum5160.mysql.rds.aliyuncs.com");
        InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, 3306);
        MysqlConnection mysqlConnection = new MysqlConnection(inetSocketAddress, "caicai", "123!@#qwe");
        try {
            mysqlConnection.connect();
            ResultSetPacket packet = mysqlConnection.query("show index from caicai.tbl_order");
            int cols = packet.getFieldDescriptors().size();
            List<String> columns = new ArrayList<>();
            for (FieldPacket fieldPacket : packet.getFieldDescriptors()) {
                columns.add(fieldPacket.getName());
            }
            int index = 0;
            List<String> values = null;
            List<List<String>> arr = new ArrayList<>();
            for (String value : packet.getFieldValues()) {
                if (index % cols == 0) {
                    values = new ArrayList<>();
                    arr.add(values);
                }
                values.add(value);
                index++;

            }
            System.out.println(arr);
        } catch (Exception e) {

        } finally {
            mysqlConnection.disconnect();
        }
    }


    public DataSourceCreator getDataSourceCreator() {
        return dataSourceCreator;
    }

    public void setDataSourceCreator(DataSourceCreator dataSourceCreator) {
        this.dataSourceCreator = dataSourceCreator;
    }
}

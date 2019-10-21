package com.caicai.ottx.service.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.parse.driver.mysql.packets.server.ResultSetPacket;
import com.alibaba.otter.canal.parse.inbound.mysql.MysqlConnection;
import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.data.DataMedia;
import com.alibaba.otter.shared.common.model.config.data.DataMediaPair;
import com.alibaba.otter.shared.common.model.config.data.DataMediaSource;
import com.alibaba.otter.shared.common.model.config.data.db.DbMediaSource;
import com.alibaba.otter.shared.common.model.config.data.mq.MqMediaSource;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.alibaba.otter.shared.common.utils.cmd.Exec;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.common.CmdMysqlConnectionUtil;
import com.caicai.ottx.common.exception.BizException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by huaseng on 2019/10/8.
 */
@Component
@Slf4j
public class ChannelDataxJobGenerator {

    private static final String BASE_DIR =   System.getProperty("user.dir");

    public static final Cache<String, Future<List<Exec.Result>>> cahceBuilder =
            CacheBuilder.newBuilder().
                    expireAfterWrite(1, TimeUnit.MINUTES)
                    .expireAfterAccess(1,TimeUnit.MINUTES)
                    .build();


    private ExecutorCompletionService<List<Exec.Result>> completionService = new ExecutorCompletionService<List<Exec.Result>>(new ThreadPoolExecutor(
            4,
            4,
            1,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<Runnable>(2),
            new ThreadPoolExecutor.DiscardOldestPolicy()
    ));

    private  List<JobModuleWrapper> createJobModule(Channel channel,String writeModel){
        List<JobModuleWrapper> jobModuleWrapperList = new ArrayList<>();
        for(Pipeline pipeline :channel.getPipelines()){
            for(DataMediaPair dataMediaPair :pipeline.getPairs()){
                jobModuleWrapperList.addAll(createJobModule(dataMediaPair,writeModel));
            }
        }
        return  jobModuleWrapperList;
    }

    private  List<JobModuleWrapper> createJobModule(DataMediaPair dataMediaPair,String writeModel){
        List<JobModuleWrapper> jobModuleWrapperList = new ArrayList<>();
        //提取源有效源数据
        String sourceNamespace = dataMediaPair.getSource().getNamespace();
        String sourceName = dataMediaPair.getSource().getName();
        sourceName = sourceName.replace(" ","");
        String names[] = sourceName.split(";");
        DbMediaSource source = (DbMediaSource)dataMediaPair.getSource().getSource();
        String sourceUrl = source.getUrl();
        String sourceUsername = source.getUsername();
        String sourcePassword = source.getPassword();
        //target
        String targetSchema = dataMediaPair.getTarget().getNamespace();
        String targetName = dataMediaPair.getTarget().getName();
        DataMediaSource targetDataMediaSource =  dataMediaPair.getTarget().getSource();
        if(targetDataMediaSource instanceof MqMediaSource){
            throw new BizException("目标数据源不能为MQ");
        }
        DbMediaSource targetMediaSource = (DbMediaSource) dataMediaPair.getTarget().getSource();
        String targetUsername = targetMediaSource.getUsername();
        String targetPassword = targetMediaSource.getPassword();
        String targetUrl = targetMediaSource.getUrl();

        if(names.length > 1){
            int index = 0;
            for(String tname:names){
                targetName = tname;
                List<String> result = CmdMysqlConnectionUtil.cmdMysqlConnection(String.format("SHOW COLUMNS FROM %s from %s",tname,sourceNamespace),source);
                String[] columns = new String[result.size()];
                System.out.println(columns);
                JobModuleWrapper jobModuleWrapper = wrapper(
                        sourceUsername,
                        sourcePassword,
                        sourceNamespace,
                        tname,
                        sourceUrl,
                        targetUsername,
                        targetPassword,
                        targetSchema,
                        targetName,
                        targetUrl,
                        result.toArray(columns),
                        writeModel
                );
                StringBuilder namewrap = new StringBuilder();
                namewrap.append(sourceNamespace)
                        .append("-")
                        .append(sourceName.split(";")[index++])
                        .append("-")
                        .append(targetSchema)
                        .append("-")
                        .append(targetName)
                        .append("-")
                        .append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))
                        .append(".json");
                jobModuleWrapper.setName(namewrap.toString());
                jobModuleWrapperList.add(jobModuleWrapper);
            }
        }else{
            if(!StringUtils.isNotBlank(targetName) || ".*".equalsIgnoreCase(targetName)){
                targetName = sourceName;
            }
            List<String> result = CmdMysqlConnectionUtil.cmdMysqlConnection(String.format("SHOW COLUMNS FROM %s from %s",sourceName,sourceNamespace),source);
            String[] columns = new String[result.size()];
            JobModuleWrapper wrapper =  wrapper(
                    sourceUsername,
                    sourcePassword,
                    sourceNamespace,
                    sourceName,
                    sourceUrl,
                    targetUsername,
                    targetPassword,
                    targetSchema,
                    targetName,
                    targetUrl,
                    result.toArray(columns),
                    writeModel
            );
            StringBuilder namewrap = new StringBuilder();
            namewrap.append(sourceNamespace)
                    .append("-")
                    .append(sourceName.split(";")[0])
                    .append("-")
                    .append(targetSchema)
                    .append("-")
                    .append(targetName)
                    .append("-")
                    .append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))
                    .append(".json");
            wrapper.setName(namewrap.toString());
            jobModuleWrapperList.add(wrapper);
        }
        return jobModuleWrapperList;
    }

    private void createJobScriptFile(JobModuleWrapper jobModuleWrapper){
        try{
            File file = new File(BASE_DIR+"/datax/job/"+jobModuleWrapper.getName());
            FileUtils.write(file,
                    jobModuleWrapper.getModelString(),
                    "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Exec.Result> processTask(Channel channel){
        List<JobModuleWrapper> jobModuleWrappers = createJobModule(channel,null);
        for(JobModuleWrapper jobModuleWrapper:jobModuleWrappers){
               createJobScriptFile(jobModuleWrapper);
        }
        return  doProcess(jobModuleWrappers);
    }


    public String processTask(DataMediaPair dataMediaPair,String writeModel){
        List<JobModuleWrapper> jobModuleWrappers = createJobModule(dataMediaPair,writeModel);
        for(JobModuleWrapper jobModuleWrapper:jobModuleWrappers){
            createJobScriptFile(jobModuleWrapper);
        }
        String requestId = UUID.randomUUID().toString();
        Future<List<Exec.Result>> future =  completionService.submit(new Callable<List<Exec.Result>>() {
            @Override
            public List<Exec.Result> call() throws Exception {
                return  doProcess(jobModuleWrappers);
            }
        });
        cahceBuilder.put(requestId,future);
        return requestId;
    }

    private List<Exec.Result> doProcess(List<JobModuleWrapper> jobModuleWrappers){
        List<Exec.Result> resultList = new ArrayList<>();
        try {
            for(JobModuleWrapper jobModuleWrapper:jobModuleWrappers){
                String cmd = BASE_DIR+"/datax/bin/datax.py "+BASE_DIR+"/datax/job/"+jobModuleWrapper.getName();
                Exec.Result result = Exec.execute(cmd);
                resultList.add(result);
            }
            return resultList;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return resultList;
        }
    }

    public String asyncCallReqeust(String requestId){
       Future<List<Exec.Result>> future =   cahceBuilder.getIfPresent(requestId);
       try{
           if(future != null){
               if(future.isDone()){
                   List<Exec.Result> results =  future.get();
                   return "finish";
               }else{
                   return "processing";
               }
           }else{
                return requestId+"不纯在";
           }
       }catch (Exception e){
           e.printStackTrace();
           log.error(e.getMessage());
           return e.getMessage();
       }
    }

    private JobModuleWrapper wrapper(String sourceUsername,String sourcePassword,String sourceSchema,String  sourceTable,String sourceUrl,String targetUsername,String targetPassword,String targetSchema,String targetTable,String targetUrl,String columns[],String writeModel){
        //一个
        JobModuleWrapper wrapper = new JobModuleWrapper();
        wrapper.setSourceUsername(sourceUsername);
        wrapper.setSourcePassword(sourcePassword);
        wrapper.setSourceUrl(sourceUrl,sourceSchema);
        wrapper.setQuerySql(sourceTable,null);
        //wrapperTarget

        if(".*".equalsIgnoreCase(targetTable) || !StringUtils.isNotBlank(targetTable)){
            targetTable = sourceTable;
        }
        if(StringUtils.isNotBlank(writeModel)){
            wrapper.setWriteModel(writeModel);
        }
        if("replace".equalsIgnoreCase(writeModel) || "update".equalsIgnoreCase(writeModel)){
            wrapper.setPreSql("");
        }else{
            wrapper.setPreSql(targetTable);
        }
        wrapper.setTargetPassword(targetPassword);
        wrapper.setTargetUsername(targetUsername);
        wrapper.setTargetUrl(targetUrl,targetSchema);
        wrapper.setTargetTable(targetTable);
        wrapper.setTargteColumns(columns);
        return wrapper;
    }

    public static void test(String[] args) {
        System.out.println(BASE_DIR);
        JSONObject jsonObject =  JSONObject.parseObject(JobModule.model);
        JSONArray jsonArray=  jsonObject.getJSONObject("job")
                .getJSONArray("content");
      JSONArray result =   jsonArray.getJSONObject(0)
        .getJSONObject("reader")
        .getJSONObject("parameter")
        .getJSONArray("connection").getJSONObject(0)
        .getJSONArray("querySql");
        result.clear();
        result.add("select * from test1");

        JSONArray jdbcUrls =   jsonArray.getJSONObject(0)
                .getJSONObject("reader")
                .getJSONObject("parameter")
                .getJSONArray("connection").getJSONObject(0)
                .getJSONArray("jdbcUrl");
        jdbcUrls.clear();
        jdbcUrls.add("jdbc:mysql://test:3306/erp");


        jsonArray.getJSONObject(0)
                .getJSONObject("reader")
                .getJSONObject("parameter").replace("username","username");
        jsonArray.getJSONObject(0)
                .getJSONObject("reader")
                .getJSONObject("parameter").replace("password","password");
        //writer
        jsonArray.getJSONObject(0)
                .getJSONObject("writer")
                .getJSONObject("parameter").replace("username","username1");
        jsonArray.getJSONObject(0)
                .getJSONObject("writer")
                .getJSONObject("parameter").replace("password","password1");

        JSONArray columns = jsonArray.getJSONObject(0)
                .getJSONObject("writer")
                .getJSONObject("parameter")
                .getJSONArray("column");
        columns.clear();
        String[] names = new String[]{"1","2","3"};
        for(String columnName:names)
            columns.add(columnName);

        JSONArray preSql = jsonArray.getJSONObject(0)
                .getJSONObject("writer")
                .getJSONObject("parameter")
                .getJSONArray("preSql");
        preSql.clear();
        preSql.add("delete from test1");


        jsonArray.getJSONObject(0)
                .getJSONObject("writer")
                .getJSONObject("parameter")
                .getJSONArray("connection")
                .getJSONObject(0)
                .replace("jdbcUrl","jdbc:mysql://test:3306/realtime_db");

       JSONArray table =  jsonArray.getJSONObject(0)
                .getJSONObject("writer")
                .getJSONObject("parameter")
                .getJSONArray("connection")
                .getJSONObject(0)
                .getJSONArray("table");
        table.clear();
        table.add("test2");

        System.out.println(jsonObject);
    }

    public static void main(String[] args) {
        String testStr = "erp_purchase_order_detail; erp_warehouse; erp_warehouse_store;";
        testStr = testStr.replace(" ","");
        System.out.println(testStr);
    }

}

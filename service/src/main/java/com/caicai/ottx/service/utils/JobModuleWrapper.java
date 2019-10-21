package com.caicai.ottx.service.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by huaseng on 2019/10/8.
 */
public class JobModuleWrapper {

    private JSONObject wrapper;
    private JSONArray jsonArray;
    private String name;

    public JobModuleWrapper(){
        this.wrapper = JSONObject.parseObject(JobModule.model);
        this.jsonArray=  wrapper.getJSONObject("job")
                .getJSONArray("content");
    }

    public JobModuleWrapper setQuerySql(String table,String orderBy){
        JSONArray result =   jsonArray.getJSONObject(0)
                .getJSONObject("reader")
                .getJSONObject("parameter")
                .getJSONArray("connection").getJSONObject(0)
                .getJSONArray("querySql");
        result.clear();
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(table);
        if(StringUtils.isNotBlank(orderBy)){
            sql.append(" order by ").append(orderBy);
        }
        result.add(sql.toString());
        return this;
    }

    public JobModuleWrapper setSourceUrl(String url,String schema){
        JSONArray jdbcUrls =   jsonArray.getJSONObject(0)
                .getJSONObject("reader")
                .getJSONObject("parameter")
                .getJSONArray("connection").getJSONObject(0)
                .getJSONArray("jdbcUrl");
        jdbcUrls.clear();
        StringBuilder jdbc = new StringBuilder();
        jdbc.append(url).append("/").append(schema);
        jdbcUrls.add(jdbc.toString());
        return this;
    }

    public JobModuleWrapper setSourceUsername(String username){
        jsonArray.getJSONObject(0)
                .getJSONObject("reader")
                .getJSONObject("parameter").replace("username",username);
        return this;
    }

    public JobModuleWrapper setSourcePassword(String password){
        jsonArray.getJSONObject(0)
                .getJSONObject("reader")
                .getJSONObject("parameter").replace("password",password);
        return this;
    }

    public JobModuleWrapper setTargetUsername(String username){
        jsonArray.getJSONObject(0)
                .getJSONObject("writer")
                .getJSONObject("parameter").replace("username",username);
        return this;
    }

    public JobModuleWrapper setTargetPassword(String password){
        jsonArray.getJSONObject(0)
                .getJSONObject("writer")
                .getJSONObject("parameter").replace("password",password);
        return this;
    }

    public JobModuleWrapper setWriteModel(String writeModel){
        jsonArray.getJSONObject(0)
                .getJSONObject("writer")
                .getJSONObject("parameter").replace("writeMode",writeModel);
        return this;
    }

    public JobModuleWrapper setTargteColumns(String[] columns){
        JSONArray column = jsonArray.getJSONObject(0)
                .getJSONObject("writer")
                .getJSONObject("parameter")
                .getJSONArray("column");
        column.clear();
        for(String columnName:columns)
            column.add(columnName);
        return this;
    }

    public JobModuleWrapper setPreSql(String table){
        JSONArray preSql = jsonArray.getJSONObject(0)
                .getJSONObject("writer")
                .getJSONObject("parameter")
                .getJSONArray("preSql");
        preSql.clear();
        StringBuilder sql = new StringBuilder();
        if(StringUtils.isNotBlank(table)){
            sql.append("delete from ").append(table);
        }
        preSql.add(sql.toString());
        return this;
    }

    public JobModuleWrapper setTargetUrl(String url,String schema){

        StringBuilder jdbc = new StringBuilder();
        jdbc.append(url).append("/").append(schema);
        jsonArray.getJSONObject(0)
                .getJSONObject("writer")
                .getJSONObject("parameter")
                .getJSONArray("connection")
                .getJSONObject(0)
                .replace("jdbcUrl",jdbc.toString());
        return this;
    }

    public JobModuleWrapper setTargetTable(String table){
        JSONArray tableArray =  jsonArray.getJSONObject(0)
                .getJSONObject("writer")
                .getJSONObject("parameter")
                .getJSONArray("connection")
                .getJSONObject(0)
                .getJSONArray("table");
        tableArray.clear();
        tableArray.add(table);
        return this;
    }

    public String getModelString(){
        return wrapper.toString();
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
}

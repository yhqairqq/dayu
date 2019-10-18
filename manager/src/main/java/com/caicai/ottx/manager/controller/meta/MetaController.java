package com.caicai.ottx.manager.controller.meta;

import com.alibaba.otter.canal.parse.driver.mysql.packets.server.ResultSetPacket;
import com.alibaba.otter.canal.parse.inbound.mysql.MysqlConnection;
import com.alibaba.otter.shared.common.model.config.data.DataMediaSource;
import com.alibaba.otter.shared.common.model.config.data.db.DbMediaSource;
import com.alibaba.otter.shared.common.model.config.data.mq.MqMediaSource;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.manager.controller.meta.form.MetaForm;
import com.caicai.ottx.service.config.datamediasource.DataMediaSourceService;
import com.mysql.jdbc.MySQLConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huaseng on 2019/9/16.
 */
@RestController
@RequestMapping("/meta")
public class MetaController {

    @Autowired
    private DataMediaSourceService dataMediaSourceService;

    @RequestMapping(value = "/schema/list",method = RequestMethod.POST)
    public ApiResult< List<String>> listSchemas(@RequestBody MetaForm metaForm){
        DataMediaSource dataMediaSource = dataMediaSourceService.findById(metaForm.getDataMediaSourceId());
        return  cmdMysqlConnection("show databases",dataMediaSource);
    }

    @RequestMapping(value = "/table/list",method = RequestMethod.POST)
    public ApiResult<List<String>> listTables(@RequestBody MetaForm metaForm){
        DataMediaSource dataMediaSource = dataMediaSourceService.findById(metaForm.getDataMediaSourceId());
        return  cmdMysqlConnection(String.format("show tables from `%s`",metaForm.getSchema()),dataMediaSource);
    }

    @RequestMapping(value = "/topic/list",method = RequestMethod.POST)
    public ApiResult<List<String>> listTopics(@RequestBody MetaForm metaForm){
        DataMediaSource dataMediaSource = dataMediaSourceService.findById(metaForm.getDataMediaSourceId());
        return  cmdMysqlConnection(String.format("show tables from `%s`",metaForm.getSchema()),dataMediaSource);
    }




    private ApiResult<List<String>> cmdMysqlConnection(String cmd, DataMediaSource dataMediaSource){
        MysqlConnection mysqlConnection = null;
        try {
            DbMediaSource dbMediaSource =   ((DbMediaSource) dataMediaSource);
            String url = dbMediaSource.getUrl();
            String host = url.substring(url.lastIndexOf("/") + 1);
            String[] arr = host.split(":");
            String domain = arr[0];
            int port = Integer.parseInt(arr[1]);
            InetAddress inetAddress = InetAddress.getByName(domain);
            InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, port);
            mysqlConnection = new MysqlConnection(inetSocketAddress, dbMediaSource.getUsername(), dbMediaSource.getPassword());
            mysqlConnection.connect();
            ResultSetPacket packet = mysqlConnection.query(cmd);
            List<String> result = new ArrayList<>();
            for (String value : packet.getFieldValues()) {
                result.add(value);
            }
            return ApiResult.success(result);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }finally {
            try {
                mysqlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

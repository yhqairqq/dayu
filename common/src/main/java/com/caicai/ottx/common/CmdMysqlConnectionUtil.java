package com.caicai.ottx.common;

import com.alibaba.otter.canal.parse.driver.mysql.packets.server.FieldPacket;
import com.alibaba.otter.canal.parse.driver.mysql.packets.server.ResultSetPacket;
import com.alibaba.otter.canal.parse.inbound.mysql.MysqlConnection;
import com.alibaba.otter.shared.common.model.config.data.DataMediaSource;
import com.alibaba.otter.shared.common.model.config.data.db.DbMediaSource;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huaseng on 2019/10/9.
 */
public class CmdMysqlConnectionUtil {

    public static  List<String> cmdMysqlConnection(String cmd, DataMediaSource dataMediaSource){
        MysqlConnection mysqlConnection = null;
        List<String> result = new ArrayList<>();
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
            if(packet.getFieldDescriptors().size()  > 1){
                int mode = packet.getFieldDescriptors().size();
                for(int i=0;i<packet.getFieldValues().size();i+=mode){
                    result.add(packet.getFieldValues().get(i));
                }
            }else{
                for (String value : packet.getFieldValues()) {
                    result.add(value);
                }
            }

            return result;
        }catch (Exception e){
            e.printStackTrace();
            return result;
        }finally {
            try {
                mysqlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}

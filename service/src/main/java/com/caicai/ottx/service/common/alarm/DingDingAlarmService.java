package com.caicai.ottx.service.common.alarm;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Created by huaseng on 2019/8/23.
 */
public class DingDingAlarmService extends AbstractAlarmService {

    private   String webhook ;
    private HttpClient httpclient = HttpClients.createDefault();
    private HttpPost httppost ;

    public void init(){
        httppost = new HttpPost(webhook);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");

    }

    @Override
    protected void doSend(AlarmMessage data) throws Exception {
        String textMsg = "{ \"msgtype\": \"text\", \"text\": {\"content\": \"" + data.getMessage() + "\"}}";
        StringEntity se = new StringEntity(textMsg, "utf-8");
        httppost.setEntity(se);
        HttpResponse response = httpclient.execute(httppost);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
        }
    }

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    public static void main(String[] args) throws Exception {
       DingDingAlarmService service = new DingDingAlarmService();
       AlarmMessage message = new AlarmMessage();
        message.setMessage("告警");
        service.doSend(message);
    }
}

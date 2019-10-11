package com.caicai.ottx.service.common.alarm;

import com.alibaba.otter.shared.common.model.config.parameter.SystemParameter;
import com.caicai.ottx.service.config.parameter.SystemParameterService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huaseng on 2019/8/23.
 */
public class DefaultAlarmService extends AbstractAlarmService {

    private static final String    TITLE = "alarm_from_otter";
    private String                 username;
    @Autowired
    private JavaMailSender         mailSender;
    @Autowired
    private SystemParameterService systemParameterService;

    public void doSend(AlarmMessage data) throws Exception {
        SimpleMailMessage mail = new SimpleMailMessage(); // 只发送纯文本
        mail.setFrom(username);
        mail.setSubject(TITLE);// 主题
        mail.setText(data.getMessage());// 邮件内容
        String receiveKeys[] = StringUtils.split(StringUtils.replace(data.getReceiveKey(), ";", ","), ",");

        SystemParameter systemParameter = systemParameterService.find();
        List<String> mailAddress = new ArrayList<String>();
        for (String receiveKey : receiveKeys) {
            String receiver = convertToReceiver(systemParameter, receiveKey);
            String strs[] = StringUtils.split(StringUtils.replace(receiver, ";", ","), ",");
            for (String str : strs) {
                if (isMail(str)) {
                    if (str != null) {
                        mailAddress.add(str);
                    }
                } else if (isSms(str)) {
                    // do nothing
                }
            }
        }

        if (!mailAddress.isEmpty()) {
            mail.setTo(mailAddress.toArray(new String[mailAddress.size()]));
            doSendMail(mail);
        }
    }

    private void doSendMail(SimpleMailMessage mail) {
        if (mailSender instanceof JavaMailSenderImpl) {
            JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;
            if (StringUtils.isNotEmpty(mailSenderImpl.getUsername())
                    && StringUtils.isNotEmpty(mailSenderImpl.getPassword())) {
                // 正确设置了账户/密码，才尝试发送邮件
                mailSender.send(mail);
            }
        }
    }

    private boolean isMail(String receiveKey) {
        return StringUtils.contains(receiveKey, '@');
    }

    private boolean isSms(String receiveKey) {
        return false;
    }

    private String convertToReceiver(SystemParameter systemParameter, String receiveKey) {
        if (StringUtils.equalsIgnoreCase(systemParameter.getDefaultAlarmReceiveKey(), receiveKey)) {
            return systemParameter.getDefaultAlarmReceiver();
        } else {
            return systemParameter.getAlarmReceiver().get(receiveKey);
        }
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}

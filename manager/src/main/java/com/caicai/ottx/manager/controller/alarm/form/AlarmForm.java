package com.caicai.ottx.manager.controller.alarm.form;

import com.caicai.ottx.manager.controller.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by huaseng on 2019/9/2.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmForm extends BaseForm {
    private Long id;
    private Long pipelineId;
    private String status;
    private String monitorName;
    private String matchValue;
    private Long intervalTime;
    private Integer recoveryThresold;
    private Date pauseTime;
    private Boolean autoRecovery;
    private String description;

}

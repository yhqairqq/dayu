package com.caicai.ottx.manager.controller.alarm.form;

import com.caicai.ottx.manager.controller.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by huaseng on 2019/9/2.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmForm extends BaseForm {
    private long pipelineId;
    private long id;
    private String status;
}

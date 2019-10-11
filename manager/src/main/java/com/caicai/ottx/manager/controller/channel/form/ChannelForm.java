package com.caicai.ottx.manager.controller.channel.form;

import com.alibaba.otter.shared.common.model.config.channel.ChannelParameter;
import com.caicai.ottx.manager.controller.BaseForm;
import com.caicai.ottx.service.config.channel.model.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * Created by huaseng on 2019/8/22.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChannelForm extends BaseForm {

    @NotNull(message = "主键不能为空",groups = {IdCheck.class,EditCheck.class})
    private Long id;

    private String            name;
    private String            description;
    private boolean           enableRemedy;                   // 是否启用冲突补救算法
    private String  remedyAlgorithm; // 冲突补救算法
    private int               remedyDelayThresoldForMedia ;                      // 低于60秒钟的同步延迟，回环补救不反查
    private String syncMode;          // 同步模式：字段/整条记录
    private String syncConsistency;    // 同步一致性要求
    public interface IdCheck{}
    public interface AddCheck{}
    public interface EditCheck{}
    private String status;
    private String start;              //描述位点开始位置
    private Tag tag;

    private String startTime;



}

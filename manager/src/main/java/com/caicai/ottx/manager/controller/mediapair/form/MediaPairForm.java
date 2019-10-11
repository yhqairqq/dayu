package com.caicai.ottx.manager.controller.mediapair.form;

import com.caicai.ottx.manager.controller.BaseForm;
import lombok.Data;

import java.util.List;

/**
 * Created by huaseng on 2019/9/10.
 */
@Data
public class MediaPairForm extends BaseForm {
    private long pipelineId;
    private long channelId;
    private long sourceId;
    private long id;
    private long targetId;
    private List<String> sourceDataMedia;
    private List<String> targetDataMedia;
    private long pushWeight;
    private String filterType;
    private String filterText;
    private String topic;
}

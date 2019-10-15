package com.caicai.ottx.manager.controller.media.form;

import com.caicai.ottx.manager.controller.BaseForm;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huaseng on 2019/10/8.
 */
@Data
public class MediaForm  extends BaseForm{
    private long sourceId;
    private String namespace;
    private String name;
    private ArrayList<String> names;
    private String topic;
    private String type;
    private String status;
    private long id;
}

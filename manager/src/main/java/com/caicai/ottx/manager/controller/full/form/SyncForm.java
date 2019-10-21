package com.caicai.ottx.manager.controller.full.form;

import lombok.Data;

/**
 * Created by huaseng on 2019/9/9.
 */
@Data
public class SyncForm {
    private String json;
    private String name;
    private long mediaPairid;
    //insert/replace/update
        private String writeMode;
    private String requestId;
}

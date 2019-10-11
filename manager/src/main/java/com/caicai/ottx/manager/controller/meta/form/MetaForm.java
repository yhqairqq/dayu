package com.caicai.ottx.manager.controller.meta.form;

import com.caicai.ottx.manager.controller.BaseForm;
import lombok.Data;

/**
 * Created by huaseng on 2019/9/16.
 */
@Data
public class MetaForm extends BaseForm {
    private Long dataMediaSourceId;
    private String schema;
}

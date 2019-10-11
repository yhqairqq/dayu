package com.caicai.ottx.manager.controller.mediasource.form;

import com.caicai.ottx.manager.controller.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by huaseng on 2019/9/2.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MediaSourceForm extends BaseForm {
    private Long id;
    private String name;
    private String url;
    private String username;
    private String password;
    private String type;
    private String encode = "UTF8";
    private String destinationName;

}

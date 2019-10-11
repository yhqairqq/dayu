package com.caicai.ottx.manager.controller.autokeeper.form;

import com.caicai.ottx.manager.controller.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by huaseng on 2019/9/2.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ZookeeperForm extends BaseForm {
    private Long id;
    private List<String> serverList;
    private String clusterName;
    private String description;
}

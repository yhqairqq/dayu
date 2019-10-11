package com.caicai.ottx.manager.controller.node.form;

import com.caicai.ottx.manager.controller.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by huaseng on 2019/8/30.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NodeForm extends BaseForm{
    private long id;
    private String name;
    private String ip;
    private Long port;
    private Integer downloadPort;
    private Integer mbeanPort;
    private String externalIp;
    private Boolean useExternalIp;
    private long autoKeeperClusterId;
    private String description;

}

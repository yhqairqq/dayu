package com.caicai.ottx.manager.controller.canal.form;

import com.caicai.ottx.manager.controller.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by huaseng on 2019/9/2.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CanalForm extends BaseForm {
    private long id;
    private String name;
    private String runMode;
    private Long zkClusterId;
    private String sourcingType;
    private String groupDbAddresses;
    private String dbUsername;
    private String dbPassword;
    private String connectionCharset;
    private String positions;
    private boolean gtidEnable;
    private boolean tsdbEnable;
    private String rdsAccesskey;
    private String rdsSecretkey;
    private String rdsInstanceId;
    private String storageMode;
    private String storageBatchMode;
    private int memoryStorageBufferSize;
    private int memoryStorageBufferMemUnit;
    private String haMode;
    private boolean detectingEnable;
    private String metaMode ;
    private String indexMode ;
    private int port;
    private int defaultConnectionTimeoutInSeconds ;
    private int receiveBufferSize ;
    private int sendBufferSize;
    private int fallbackIntervalInSeconds;
    private String blackFilter;
    private String desc;
    private String destinationName;
    private String url;
}

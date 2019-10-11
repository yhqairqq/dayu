/**
 *
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 *
 * PipelineNodeRelationDO.java
 *
 */
package com.caicai.ottx.dal.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : pipeline_node_relation的 model 类
 *
 * @author 	Yanghq
 * @date 	2019年08月23日
 */
@Data
@Alias("PipelineNodeRelationDO")
public class PipelineNodeRelationDO implements Serializable {

    private static final long serialVersionUID = -2066978336563209425L;
    private Long              id;
    private Long              nodeId;
    private Long              PipelineId;
    private Location          location;                                // 表示Node位于该pipeline的源或是目的
    private Date              gmtCreate;
    private Date              gmtModified;

    public static enum Location {
        SELECT, EXTRACT, LOAD;

        public boolean isSelect() {
            return this.equals(Location.SELECT);
        }

        public boolean isExtract() {
            return this.equals(Location.EXTRACT);
        }

        public boolean isLoad() {
            return this.equals(Location.LOAD);
        }
    }

}

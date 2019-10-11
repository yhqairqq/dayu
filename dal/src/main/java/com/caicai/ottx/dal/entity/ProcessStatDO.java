package com.caicai.ottx.dal.entity;

import com.alibaba.otter.shared.common.model.statistics.stage.StageStat;
import com.alibaba.otter.shared.common.utils.OtterToStringStyle;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huaseng on 2019/8/23.
 */
@Setter
@Getter
@Alias("ProcessStatDO")
public class ProcessStatDO implements Serializable {
    private static final long serialVersionUID = -5625269232233751756L;
    private Long              pipelineId;
    private Long              processId;
    private List<StageStat> stageStats;                              // 当前process的阶段列表
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, OtterToStringStyle.DEFAULT_STYLE);
    }
}

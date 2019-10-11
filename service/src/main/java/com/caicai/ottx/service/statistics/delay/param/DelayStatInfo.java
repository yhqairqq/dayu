package com.caicai.ottx.service.statistics.delay.param;

import com.alibaba.otter.shared.common.model.statistics.delay.DelayStat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huaseng on 2019/9/2.
 */
@Setter
@Getter
public class DelayStatInfo implements Serializable {
    private static final long serialVersionUID = 6963357116856377131L;
    private List<DelayStat> items;

    /**
     * 一段时间内堆积量的平均值统计
     */

    public Double getAvgDelayNumber() {
        Double avgDelayNumber = 0.0;
        if (items.size() != 0) {
            for (DelayStat item : items) {
                avgDelayNumber += item.getDelayNumber();
            }
            avgDelayNumber = avgDelayNumber / items.size();
        }
        return avgDelayNumber;
    }

    /**
     * 一段时间内延迟时间的平均值统计
     */

    public Double getAvgDelayTime() {
        Double avgDelayTime = 0.0;
        if (items.size() != 0) {
            for (DelayStat item : items) {
                avgDelayTime += item.getDelayTime();
            }
            avgDelayTime = avgDelayTime / items.size();
        }
        return avgDelayTime;
    }



}

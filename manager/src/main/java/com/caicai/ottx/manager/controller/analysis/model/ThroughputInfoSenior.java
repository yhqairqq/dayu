package com.caicai.ottx.manager.controller.analysis.model;

import com.caicai.ottx.service.statistics.table.param.ThroughputInfo;
import lombok.Data;

/**
 * Created by huaseng on 2019/9/27.
 */
@Data
public class ThroughputInfoSenior extends ThroughputInfo{

    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

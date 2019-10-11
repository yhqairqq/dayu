package com.caicai.ottx.manager.controller.analysis.model;

import lombok.Data;

/**
 * Created by huaseng on 2019/9/27.
 */
@Data
public class PlotCell {
    private long time;   //截止时刻
    private long num;   //记录条数
    private long size;  //大小
    private long quantity;
    private double avgDelayNumber;
    private double avgDelayTime;
}

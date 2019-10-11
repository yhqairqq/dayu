package com.caicai.ottx.common.vo;

import lombok.Data;

import java.util.Map;

/**
 * Created by huaseng on 2019/8/22.
 */
@Data
public class BaseForm {
    protected Map<String, Object> params; // 查询条件
    protected Integer pageSize    = 10;  // 一页显示条数
    protected Integer currentPage = 1;   // 页号从1开始
}

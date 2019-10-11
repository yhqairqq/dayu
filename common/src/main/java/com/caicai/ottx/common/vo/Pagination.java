package com.caicai.ottx.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by huaseng on 2019/8/29.
 */
@Data
@NoArgsConstructor
public class Pagination implements Serializable {

    public Pagination(int total, int pageSize, int current) {
        this.total = total;
        this.pageSize = pageSize;
        this.current = current;
    }
    private int total = 0;
    private int pageSize;
    private int current = 1;
}


package com.caicai.ottx.common.vo;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huaseng on 2019/8/29.
 */
@Data
public class PageResult<T> {
    private List<T> list;
    private Pagination pagination;

    public PageResult(List<T> list,Page<T> page){
        if (page != null) {
            this.list = list;
            this.pagination = new Pagination(
                    (int)page.getTotal(),
                    page.getPageSize(),
                    page.getPageNum()
            );
        } else {
            this.list = new ArrayList<>();
            this.pagination = new Pagination();
        }
    }
}

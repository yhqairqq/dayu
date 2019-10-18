package com.caicai.ottx.service.trans.model;

import com.alibaba.otter.shared.common.model.config.data.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huaseng on 2019/10/18.
 */
@Data
public class DataMediaPairTrans {
    private static final long serialVersionUID = 6173105172139714032L;
    private Long              id;
    private DataMedia         source;
    private DataMedia         target;
    private ColumnPairMode columnPairMode   = ColumnPairMode.INCLUDE;
    private List<ColumnPair> columnPairs      = new ArrayList<ColumnPair>();
    private List<ColumnGroup> columnGroups     = new ArrayList<ColumnGroup>();
    private Date               gmtCreate;
    private Date              gmtModified;
}

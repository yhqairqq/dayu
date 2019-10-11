package com.caicai.ottx.service.config.datacolumnpair;

import com.alibaba.otter.manager.biz.common.baseservice.GenericService;
import com.alibaba.otter.shared.common.model.config.data.ColumnGroup;

import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface DataColumnPairGroupService extends GenericService<ColumnGroup> {
     void removeByDataMediaPairId(Long dataMediaPairId);

     List<ColumnGroup> listByDataMediaPairId(Long dataMediaPairId);

     Map<Long, List<ColumnGroup>> listByDataMediaPairIds(Long... dataMediaPairId);

}

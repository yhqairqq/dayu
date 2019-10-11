package com.caicai.ottx.service.config.datacolumnpair;

import com.alibaba.otter.manager.biz.common.baseservice.GenericService;
import com.alibaba.otter.shared.common.model.config.data.ColumnPair;

import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface DataColumnPairService  extends GenericService<ColumnPair> {
     List<ColumnPair> listByDataMediaPairId(Long dataMediaPairId);

     Map<Long, List<ColumnPair>> listByDataMediaPairIds(Long... dataMediaPairIds);

     void createBatch(List<ColumnPair> dataColumnPairs);

     void removeByDataMediaPairId(Long dataMediaPairId);
}

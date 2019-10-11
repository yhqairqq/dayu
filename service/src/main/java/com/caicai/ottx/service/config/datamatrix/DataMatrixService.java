package com.caicai.ottx.service.config.datamatrix;

import com.alibaba.otter.shared.common.model.config.data.DataMatrix;

import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface DataMatrixService {
     void create(DataMatrix DataMatrix);

     void remove(Long DataMatrixId);

     void modify(DataMatrix DataMatrix);

     List<DataMatrix> listByIds(Long... identities);

     List<DataMatrix> listAll();

     DataMatrix findById(Long DataMatrixId);

     DataMatrix findByGroupKey(String name);

     int getCount(Map condition);

     List<DataMatrix> listByCondition(Map condition);
}

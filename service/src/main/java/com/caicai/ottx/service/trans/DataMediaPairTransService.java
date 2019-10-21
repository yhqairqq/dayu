package com.caicai.ottx.service.trans;

import com.caicai.ottx.service.trans.model.DataMediaPairTrans;

import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/10/18.
 */
public interface DataMediaPairTransService {
    void create(DataMediaPairTrans dataMediaPairTrans);


    void remove(Long canalId);

    void modify(DataMediaPairTrans dataMediaPairTrans);

    List<DataMediaPairTrans> listByIds(Long... identities);

    List<DataMediaPairTrans> listAll();

    DataMediaPairTrans findById(Long canalId);

    DataMediaPairTrans findByName(String name);

    int getCount(Map condition);

    List<DataMediaPairTrans> listByCondition(Map condition);

    long createAndReturnId(DataMediaPairTrans dataMediaPairTrans);
}

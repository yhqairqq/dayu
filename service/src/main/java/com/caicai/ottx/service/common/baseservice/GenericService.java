package com.caicai.ottx.service.common.baseservice;

import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface GenericService<T> {
     void create(T entityObj);

     void remove(Long identity);

     void modify(T entityObj);

     T findById(Long identity);

     List<T> listByIds(Long... identities);

     List<T> listAll();

     List<T> listByCondition(Map condition);

     int getCount();

     int getCount(Map condition);
}

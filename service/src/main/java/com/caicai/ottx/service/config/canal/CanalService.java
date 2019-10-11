package com.caicai.ottx.service.config.canal;

import com.alibaba.otter.canal.instance.manager.model.Canal;

import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface CanalService {
     void create(Canal canal);

     void remove(Long canalId);

     void modify(Canal canal);

     List<Canal> listByIds(Long... identities);

     List<Canal> listAll();

     Canal findById(Long canalId);

     Canal findByName(String name);

     int getCount(Map condition);

     List<Canal> listByCondition(Map condition);
}

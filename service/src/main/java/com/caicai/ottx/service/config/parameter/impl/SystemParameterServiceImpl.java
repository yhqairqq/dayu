package com.caicai.ottx.service.config.parameter.impl;

import com.alibaba.otter.shared.common.model.config.parameter.SystemParameter;
import com.caicai.ottx.dal.entity.SystemParameterDO;
import com.caicai.ottx.service.config.parameter.SystemParameterService;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by huaseng on 2019/8/23.
 */
@Service
public class SystemParameterServiceImpl implements SystemParameterService{
    private static final Logger logger = LoggerFactory.getLogger(SystemParameterServiceImpl.class);

    @Autowired
    private SqlSession sqlSession;

    @Override
    public void createOrUpdate(SystemParameter systemParameter) {

    }

    @Override
    public SystemParameter find() {
        List<SystemParameterDO> systemParameterDos = sqlSession.selectList("listParameters");

                //systemParameterDao.listAll();
        if (CollectionUtils.isEmpty(systemParameterDos)) {
            logger.debug("DEBUG ## couldn't query any SystemParameter, maybe hasn't create any SystemParameter.");
            return new SystemParameter();
        } else {
            return doToModel(systemParameterDos.get(0));
        }
    }
    private SystemParameter doToModel(SystemParameterDO systemParameterDo) {
        return systemParameterDo.getValue();
    }
}

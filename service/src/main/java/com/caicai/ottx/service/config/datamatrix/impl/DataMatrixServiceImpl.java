package com.caicai.ottx.service.config.datamatrix.impl;

import com.alibaba.otter.shared.common.model.config.data.DataMatrix;
import com.alibaba.otter.shared.common.utils.Assert;
import com.caicai.ottx.dal.entity.DataMatrixDO;
import com.caicai.ottx.dal.mapper.DataMatrixDOMapperExt;
import com.caicai.ottx.service.common.exceptions.ManagerException;
import com.caicai.ottx.service.config.datamatrix.DataMatrixService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/9/21.
 */
@Service
@Slf4j
public class DataMatrixServiceImpl implements DataMatrixService{

    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private DataMatrixDOMapperExt dataMatrixDOMapperExt;


    @Override
    public void create(DataMatrix DataMatrix) {

    }

    @Override
    public void remove(Long DataMatrixId) {

    }

    @Override
    public void modify(DataMatrix DataMatrix) {

    }

    @Override
    public List<DataMatrix> listByIds(Long... identities) {
        return null;
    }

    @Override
    public List<DataMatrix> listAll() {
        return null;
    }

    @Override
    public DataMatrix findById(Long DataMatrixId) {
        return null;
    }

    @Override
    public DataMatrix findByGroupKey(String groupKey) {
        Assert.assertNotNull(groupKey);
        DataMatrixDO matrixDo = sqlSession.selectOne("findDataMatrixByGroupKey",groupKey);
//                dataMatrixDao.findByGroupKey(groupKey);
        if (matrixDo == null) {
            String exceptionCause = "query name:" + groupKey + " return null.";
            log.error("ERROR ## " + exceptionCause);
            throw new ManagerException(exceptionCause);
        }

        return doToModel(matrixDo);
    }

    private DataMatrix doToModel(DataMatrixDO matrixDo) {
        DataMatrix matrix = new DataMatrix();
        try {
            matrix.setId(matrixDo.getId());
            matrix.setGroupKey(matrixDo.getGroupKey());
            matrix.setDescription(matrixDo.getDescription());
            matrix.setMaster(matrixDo.getMaster());
            matrix.setSlave(matrixDo.getSlave());
            matrix.setGmtCreate(matrixDo.getGmtCreate());
            matrix.setGmtModified(matrixDo.getGmtModified());
        } catch (Exception e) {
            log.error("ERROR ## change the canal Do to Model has an exception");
            throw new ManagerException(e);
        }

        return matrix;
    }
    @Override
    public int getCount(Map condition) {
        return 0;
    }

    @Override
    public List<DataMatrix> listByCondition(Map condition) {
        return null;
    }
}

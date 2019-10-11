package com.caicai.ottx.service.config.canal.impl;


import com.alibaba.otter.canal.instance.manager.model.Canal;
import com.alibaba.otter.canal.instance.manager.model.CanalParameter;
import com.alibaba.otter.shared.arbitrate.ArbitrateViewService;
import com.alibaba.otter.shared.common.model.autokeeper.AutoKeeperCluster;
import com.alibaba.otter.shared.common.utils.Assert;
import com.alibaba.otter.shared.common.utils.JsonUtils;
import com.caicai.ottx.dal.entity.CanalDO;
import com.caicai.ottx.dal.mapper.CanalDOMapperExt;
import com.caicai.ottx.service.common.exceptions.ManagerException;
import com.caicai.ottx.service.common.exceptions.RepeatConfigureException;
import com.caicai.ottx.service.config.autokeeper.AutoKeeperClusterService;
import com.caicai.ottx.service.config.canal.CanalService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/23.
 */
@Service
@Slf4j
public class CanalServiceImpl implements CanalService {

    @Autowired
    private SqlSession sqlSession;

    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private AutoKeeperClusterService autoKeeperClusterService;
    @Autowired
    private ArbitrateViewService arbitrateViewService;
    @Autowired
    private CanalDOMapperExt canalDOMapperExt;

    @Override
    public void create(Canal canal) {
        Assert.assertNotNull(canal);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            protected void doInTransactionWithoutResult(TransactionStatus status) {

                try {
                    CanalDO canalDO = modelToDo(canal);
                    canalDO.setId(0L);
                    if (!checkUnique(canalDO)) {
                        String exceptionCause = "exist the same repeat canal in the database.";
                        log.warn("WARN ## " + exceptionCause);
                        throw new RepeatConfigureException(exceptionCause);
                    }
                    canalDOMapperExt.insertSelective(canalDO);
                    canal.setId(canalDO.getId());
                } catch (RepeatConfigureException rce) {
                    throw rce;
                } catch (Exception e) {
                    log.error("ERROR ## create canal has an exception!");
                    throw new ManagerException(e);
                }
            }
        });
    }

    @Override
    public void remove(Long canalId) {
        Assert.assertNotNull(canalId);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            protected void doInTransactionWithoutResult(TransactionStatus status) {

                try {
                    Canal canal = findById(canalId);
                    canalDOMapperExt.deleteByPrimaryKey(canalId);
                    arbitrateViewService.removeCanal(canal.getName()); // 删除canal节点信息
                } catch (Exception e) {
                    log.error("ERROR ## remove canal(" + canalId + ") has an exception!");
                    throw new ManagerException(e);
                }
            }
        });
    }

    private boolean checkUnique(CanalDO canalDO){
        int count =  sqlSession.selectOne("checkUniqueCanal",canalDO);
        return count == 0 ? true : false;
    }

    @Override
    public void modify(Canal canal) {
        Assert.assertNotNull(canal);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            protected void doInTransactionWithoutResult(TransactionStatus status) {

                try {
                    CanalDO canalDo = modelToDo(canal);
                    if (checkUnique(canalDo)) {
                        canalDOMapperExt.updateByPrimaryKeySelective(canalDo);
                    } else {
                        String exceptionCause = "exist the same repeat canal in the database.";
                        log.warn("WARN ## " + exceptionCause);
                        throw new RepeatConfigureException(exceptionCause);
                    }
                } catch (RepeatConfigureException rce) {
                    rce.printStackTrace();
                    throw rce;
                } catch (Exception e) {
                    log.error("ERROR ## modify canal(" + canal.getId() + ") has an exception!");
                    throw new ManagerException(e);
                }
            }
        });
    }
    private CanalDO modelToDo(Canal canal) {
       CanalDO canalDo = new CanalDO();
        try {
            canalDo.setId(canal.getId());
            canalDo.setName(canal.getName());
            canalDo.setStatus(canal.getStatus());
            canalDo.setDescription(canal.getDesc());
            canalDo.setParameters(canal.getCanalParameter());
            canalDo.setGmtCreate(canal.getGmtCreate());
            canalDo.setGmtModified(canal.getGmtModified());
        } catch (Exception e) {
            log.error("ERROR ## change the canal Model to Do has an exception");
            throw new ManagerException(e);
        }
        return canalDo;
    }

    @Override
    public List<Canal> listByIds(Long... identities) {
        List<Canal> canals = new ArrayList<Canal>();
        try {
            List<CanalDO> canalDos = null;
            if (identities.length < 1) {
                canalDos = sqlSession.selectList("listAllCanal");
                if (canalDos.isEmpty()) {
                    log.debug("DEBUG ## couldn't query any canal, maybe hasn't create any canal.");
                    return canals;
                }
            } else {
                canalDos = sqlSession.selectList("listCanalByIds",identities);
                        //canalDao.listByMultiId(identities);
                if (canalDos.isEmpty()) {
                    String exceptionCause = "couldn't query any canal by canalIds:" + Arrays.toString(identities);
                    log.error("ERROR ## " + exceptionCause);
                    throw new ManagerException(exceptionCause);
                }
            }
            canals = doToModel(canalDos);
        } catch (Exception e) {
            log.error("ERROR ## query channels has an exception!");
            throw new ManagerException(e);
        }

        return canals;
    }

    @Override
    public List<Canal> listAll() {
        return listByIds();
    }

    @Override
    public Canal findById(Long canalId) {
        CanalDO canalDO =  canalDOMapperExt.selectByPrimaryKey(canalId);
        return  doToModel(canalDO);

    }

    @Override
    public Canal findByName(String name) {
        Assert.assertNotNull(name);
        CanalDO canalDo = sqlSession.selectOne("findCanalByName",name);
//              canalDao.findByName(name);
        if (canalDo == null) {
            String exceptionCause = "query name:" + name + " return null.";
            log.error("ERROR ## " + exceptionCause);
            throw new ManagerException(exceptionCause);
        }
        return doToModel(canalDo);
    }

    @Override
    public int getCount(Map condition) {
        return 0;
    }

    @Override
    public List<Canal> listByCondition(Map condition) {
        PageHelper.startPage((int)condition.get("current"),(int)condition.get("pageSize"));
        List<CanalDO> canalDos = sqlSession.selectList("listCanals",condition);
                //canalDao.listByCondition(condition);
        if (CollectionUtils.isEmpty(canalDos)) {
            log.debug("DEBUG ## couldn't query any canal by the condition:" + JsonUtils.marshalToString(condition));
            return new Page<Canal>();
        }

        List<Canal> canalList =  doToModel(canalDos);
        Page<Canal> page = new Page<Canal>();
        Page<Canal> pageTemp = (Page)canalDos;
        page.addAll(canalList);
        page.setPageSize(pageTemp.getPageSize());
        page.setPageNum(pageTemp.getPageNum());
        page.setTotal(pageTemp.getTotal());
        return page;
    }
    private List<Canal> doToModel(List<CanalDO> canalDos) {
        List<Canal> canals = new ArrayList<Canal>();
        for (CanalDO canalDo : canalDos) {
            canals.add(doToModel(canalDo));
        }
        return canals;
    }
    private Canal doToModel(CanalDO canalDo) {
        Canal canal = new Canal();
        try {
            canal.setId(canalDo.getId());
            canal.setName(canalDo.getName());
            canal.setStatus(canalDo.getStatus());
            canal.setDesc(canalDo.getDescription());
            CanalParameter parameter = canalDo.getParameters();
            AutoKeeperCluster zkCluster = autoKeeperClusterService.findAutoKeeperClusterById(parameter.getZkClusterId());
            if (zkCluster != null) {
                parameter.setZkClusters(Arrays.asList(StringUtils.join(zkCluster.getServerList(), ',')));
            }
            canal.setCanalParameter(canalDo.getParameters());
            canal.setGmtCreate(canalDo.getGmtCreate());
            canal.setGmtModified(canalDo.getGmtModified());
        } catch (Exception e) {
            log.error("ERROR ## change the canal Do to Model has an exception");
            throw new ManagerException(e);
        }

        return canal;
    }
}

package com.caicai.ottx.service.config.sys.impl;

import com.caicai.ottx.common.vo.MenuVo;
import com.caicai.ottx.common.vo.SysResourceVO;
import com.caicai.ottx.dal.entity.SysResourceDO;
import com.caicai.ottx.service.config.sys.SysResourceService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Created by huaseng on 2019/8/19.
 */
@Service
public class SysResourceServiceImpl implements SysResourceService {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<SysResourceDO> listRoots() {
        return sqlSession.selectList("listRoots");
    }

    @Override
    public List<SysResourceDO> listAll() {
        return sqlSession.selectList("listAll");
    }

    @Override
    public List<MenuVo> getMenus() {
        List<SysResourceDO> sysResourceDOS = listAll();
        List<MenuVo> menuVos = new ArrayList<>();
        Map<Long,MenuVo> map = new HashMap<>();
        //获取root节点
        for(SysResourceDO sysResourceDO:sysResourceDOS){
            if(sysResourceDO.getParentId() == 0){
                map.put(sysResourceDO.getId(),new MenuVo(sysResourceDO.getPath(),sysResourceDO.getName(),sysResourceDO.getIcon()));
            }
        }
        for(SysResourceDO sysResourceDO:sysResourceDOS){
            if(sysResourceDO.getParentId() == 0){
                continue;
            }
            Long pid = sysResourceDO.getParentId();
            if(map.containsKey(pid)){
                if(map.get(pid).getChildren() == null){
                    map.get(pid).setChildren(new ArrayList<>());
                }
                map.get(pid).getChildren().add(new MenuVo(sysResourceDO.getPath(),sysResourceDO.getName(),sysResourceDO.getIcon()));
            }
        }
        return map.values().stream().collect(Collectors.toList());
    }


    private List<SysResourceVO> doToModel(List<SysResourceDO> sysResourceDOs){
        List<SysResourceVO> sysResourceVOS = new ArrayList<>();
        for(SysResourceDO sysResourceDO:sysResourceDOs){
            sysResourceVOS.add(doToModel(sysResourceDO));
        }
        return sysResourceVOS;
    }
    private SysResourceVO doToModel(SysResourceDO sysResourceDO){
        SysResourceVO sysResourceVO = new SysResourceVO();
        try{

        }catch (Exception e){

        }
        return sysResourceVO;
    }
}

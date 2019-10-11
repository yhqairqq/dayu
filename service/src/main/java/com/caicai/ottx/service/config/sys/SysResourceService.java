package com.caicai.ottx.service.config.sys;

import com.caicai.ottx.common.vo.MenuVo;
import com.caicai.ottx.dal.entity.SysResourceDO;

import java.util.List;

/**
 * Created by huaseng on 2019/8/19.
 */
public interface SysResourceService {
    List<SysResourceDO> listRoots();
    List<SysResourceDO> listAll();
    List<MenuVo> getMenus();

}

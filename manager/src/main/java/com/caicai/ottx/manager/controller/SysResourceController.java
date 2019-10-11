package com.caicai.ottx.manager.controller;

import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.common.vo.MenuVo;
import com.caicai.ottx.service.config.sys.SysResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by huaseng on 2019/8/19.
 */
@RestController
@RequestMapping(value = "/sys")
@Slf4j
public class SysResourceController {
    @Autowired
    private SysResourceService sysResourceService;

    @RequestMapping(value = "/menuTree")
    public ApiResult<List<MenuVo>> getUserMenuTree(){
        try{
            return ApiResult.success(sysResourceService.getMenus());
        }catch (Exception e){
            log.error(e.getMessage());
            return ApiResult.failed(e.getMessage());
        }
    }
}

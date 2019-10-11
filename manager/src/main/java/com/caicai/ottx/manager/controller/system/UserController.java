package com.caicai.ottx.manager.controller.system;

import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.common.ErrorCode;
import com.caicai.ottx.common.exception.BizException;
import com.caicai.ottx.common.system.UserVo;
import com.caicai.ottx.common.utils.BeanUtils;
import com.caicai.ottx.common.vo.MenuVo;
import com.caicai.ottx.dal.entity.UserDO;
import com.caicai.ottx.manager.controller.system.form.LoginForm;
import com.caicai.ottx.service.config.sys.SysResourceService;
import com.caicai.ottx.service.config.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/20.
 */
@RequestMapping("/sys/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SysResourceService sysResourceService;

    @RequestMapping("/login")
    public ApiResult doLogin(@RequestBody @Validated(value = LoginForm.LoginCheck.class) LoginForm loginForm) {
        UserVo vo = userService.login(loginForm.getUserName(), loginForm.getPassword());
        if (vo == null) {
            return ApiResult.failed(ErrorCode.EC_401,"登录失败");
        }

        // 获取资源权限
        Map<String, Object> map;
        try {
            map = BeanUtils.objectToMap(vo);
//            Map<String, List<Integer>> optPermissions = iAccountService.getResOptPermission(vo.getId());

//            map.put("optPermissions", optPermissions);
            return new ApiResult<>(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResult.success(vo);
    }
    /**
     * 获取当前用户的菜单列表
     *
     * @return List
     */
    @RequestMapping("/menuTree")
    public ApiResult getUserMenuTree() {
        List<MenuVo> menus =    sysResourceService.getMenus();
        return ApiResult.success(menus);
    }
    @RequestMapping("/getCurrent")
    public ApiResult<UserVo> getCurrent() {
        UserDO userDO = userService.getCurrentUser();
        if (userDO == null) {
            throw new BizException(ErrorCode.EC_401,ErrorCode.EM_401);
        }
        UserVo vo = new UserVo();
        org.springframework.beans.BeanUtils.copyProperties(userDO, vo);
        return new ApiResult<>(vo);
    }


}

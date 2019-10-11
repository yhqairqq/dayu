package com.caicai.ottx.manager.controller.system;

import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.service.config.parameter.SystemParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by huaseng on 2019/8/28.
 */
@RestController
@RequestMapping("/sys/parameter")
@Slf4j
public class SystemParameterController {
    @Autowired
    private SystemParameterService systemParameterService;

    @RequestMapping(value = "/reduction", method = RequestMethod.GET)
    public ApiResult<String> reduction() {
        return ApiResult.success("");
    }
}

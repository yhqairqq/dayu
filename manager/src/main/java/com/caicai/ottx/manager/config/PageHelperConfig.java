package com.caicai.ottx.manager.config;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by huaseng on 2019/8/22.
 */
@Configuration
public class PageHelperConfig {
    @Bean
    public PageHelper pageHelper(){
        return new PageHelper();
    }
}

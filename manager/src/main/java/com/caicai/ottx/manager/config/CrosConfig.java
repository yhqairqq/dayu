package com.caicai.ottx.manager.config;

import com.caicai.ottx.manager.filter.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by huaseng on 2019/8/20.
 */
@Configuration
public class CrosConfig {
    @Bean
    public FilterRegistrationBean customizeFilter(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CorsFilter());
        return registration;
    }
}

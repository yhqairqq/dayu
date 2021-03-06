package com.caicai.ottx.manager.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.caicai.ottx.manager.aspect.TokenVerifyInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2019-05-23 11:57.
 * Modify:
 */
@Configuration
//@ImportResource(locations = {"classpath:config/default-uid-spring.xml"})
public class WebConfig extends WebMvcConfigurationSupport {

//    @Autowired
//    private CorsProperties corsProperties;
//
//
//    @Override
//    protected void addCorsMappings(CorsRegistry registry) {
//        //允许的域
//        List<String> tmps = corsProperties.getAllowOrigin();
//        String[] allowedOrigins;
//        if (tmps == null || tmps.size() <= 0) {
//            allowedOrigins = new String[1];
//            allowedOrigins[0] = "*";
//        } else {
//            allowedOrigins = tmps.toArray(new String[tmps.size()]);
//        }
//
//        //允许的方法
//        tmps = corsProperties.getAllowMethods();
//        String[] allowMethods;
//        if (tmps == null || tmps.size() <= 0) {
//            allowMethods = new String[1];
//            allowMethods[0] = "*";
//        } else {
//            allowMethods = tmps.toArray(new String[tmps.size()]);
//        }
//
//        //允许的头
//        tmps = corsProperties.getAllowHeaders();
//        String[] allowHeaders;
//        if (tmps == null || tmps.size() <= 0) {
//            allowHeaders = new String[1];
//            allowHeaders[0] = "*";
//        } else {
//            allowHeaders = tmps.toArray(new String[tmps.size()]);
//        }
//
//        registry.addMapping("/**")
//                .allowedOrigins(allowedOrigins)
//                .allowedMethods(allowMethods)
//                .allowedHeaders(allowHeaders)
//                .exposedHeaders("Content-Disposition")
//                .allowCredentials(corsProperties.isAllowCredentials())
//                .maxAge(3600);
//    }

    /**
     * 修改自定义消息转换器
     *
     * @param converters List
     */
    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //调用父类的配置
        super.configureMessageConverters(converters);

        // 创建fastJson消息转换器
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();


        //升级最新版本需加=============================================================
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        fastConverter.setSupportedMediaTypes(supportedMediaTypes);

        // 初始化一个转换器配置
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

        //修改配置返回内容的过滤
        //WriteNullListAsEmpty  ：List字段如果为null,输出为[],而非null
        //WriteNullStringAsEmpty ： 字符类型字段如果为null,输出为"",而非null
        //DisableCircularReferenceDetect ：消除对同一对象循环引用的问题，默认为false（如果不配置有可能会进入死循环）
        //WriteNullBooleanAsFalse：Boolean字段如果为null,输出为false,而非null
        //WriteMapNullValue：是否输出值为null的字段,默认为false
        fastJsonConfig.setSerializerFeatures(SerializerFeature.BrowserCompatible,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.DisableCircularReferenceDetect);


        //解决Long转json精度丢失的问题
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        fastJsonConfig.setSerializeConfig(serializeConfig);
        // 将配置设置给转换器并添加到HttpMessageConverter转换器列表中
        fastConverter.setFastJsonConfig(fastJsonConfig);

        //将fastjson添加到视图消息转换器列表内
        converters.add(fastConverter);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenVerifyInterceptor());
        super.addInterceptors(registry);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    @Bean
    public MultipartResolver multipartResolver(){
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setMaxUploadSize(102400000);
        resolver.setMaxInMemorySize(4096);
        return resolver;
    }
}

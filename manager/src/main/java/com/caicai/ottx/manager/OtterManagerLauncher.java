/*
 * Copyright (C) 2010-2101 Alibaba Group Holding Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.caicai.ottx.manager;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.FileInputStream;
import java.util.Map.Entry;
import java.util.Properties;
@SpringBootApplication
@ImportResource(value = {"classpath*:applicationContext.xml"})
@ComponentScan(basePackages = "com.caicai.ottx")
@MapperScan(basePackages = {"com.caicai.ottx.dal.mapper"})
@EnableWebMvc
public class OtterManagerLauncher extends SpringBootServletInitializer {

    private static final Logger logger      = LoggerFactory.getLogger(OtterManagerLauncher.class);
    private static final String CLASSPATH_URL_PREFIX = "classpath:";

    public static void main(String[] args) throws Throwable {

        try {
            String conf = System.getProperty("otter.conf", "classpath:otter.properties");
            Properties properties = new Properties();
            if (conf.startsWith(CLASSPATH_URL_PREFIX)) {
                conf = StringUtils.substringAfter(conf, CLASSPATH_URL_PREFIX);
                properties.load(OtterManagerLauncher.class.getClassLoader().getResourceAsStream(conf));
            } else {
                properties.load(new FileInputStream(conf));
            }

            // 合并配置到system参数中
            mergeProps(properties);

            logger.info("## start the manager server.");
//            final JettyEmbedServer server = new JettyEmbedServer(properties.getProperty("otter.jetty", "jetty.xml"));
//            server.start();
            logger.info("## the manager server is running now ......");
            Runtime.getRuntime().addShutdownHook(new Thread() {

                public void run() {
                    try {
                        logger.info("## stop the manager server");
//                        server.join();
                    } catch (Throwable e) {
                        logger.warn("##something goes wrong when stopping manager Server:\n{}",
                                    ExceptionUtils.getFullStackTrace(e));
                    } finally {
                        logger.info("## manager server is down.");
                    }
                }

            });
        } catch (Throwable e) {
            logger.error("## Something goes wrong when starting up the manager Server:\n{}",
                         ExceptionUtils.getFullStackTrace(e));
            System.exit(0);
        }

        SpringApplication.run(OtterManagerLauncher.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(OtterManagerLauncher.class);
    }
    private static void mergeProps(Properties props) {
        for (Entry<Object, Object> entry : props.entrySet()) {
            System.setProperty((String) entry.getKey(), (String) entry.getValue());
        }
    }
}

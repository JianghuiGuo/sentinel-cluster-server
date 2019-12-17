package com.sentinel.server.core;

import com.misyi.architecture.commons.cloud.configuration.CloudConfiguration;
import com.misyi.architecture.commons.web.configuration.WebMvcConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@Import({ WebMvcConfig.class, CloudConfiguration.class})
//@MapperScan(basePackages = "com.misyi.exercise.core.mapper")
//@ComponentScan(basePackages = {"com.misyi.exercise.core"})   Application 类同包会默认被扫描
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

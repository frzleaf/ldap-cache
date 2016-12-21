package com.tecapro.ldap.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created by chai65 on 12/20/2016.
 */

@SpringBootApplication
@EnableSwagger2
public class Launcher {


    public static void main(String[] args) {
        SpringApplication.run(Launcher.class, args);
    }


    @Bean
    public Docket apis(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(regex("/.*"))
                .build();
    }

}

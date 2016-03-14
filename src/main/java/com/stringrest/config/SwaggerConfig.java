package com.stringrest.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    TypeResolver typeResolver;

    @Bean
    Docket basicInfo() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(new ApiInfoBuilder()
                .title("String Set REST API")
                .description("A demo project for java developer, fast track")
                .build())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(apiPaths())
                .build();
    }

    //Here is an example where we select any api that matches one of these paths
    @SuppressWarnings("unchecked")
    private Predicate<String> apiPaths() {
        return Predicates.or(
                regex("/stringsets.*"),
                regex("/stats.*"));
    }
}

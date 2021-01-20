package com.app.infrastructure.mongo.initscripts.config;

import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.SpringDataMongoV3Driver;

import io.changock.runner.spring.v5.SpringInitializingBeanRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import static com.github.cloudyrock.spring.v5.MongockSpring5.builder;

@Configuration
public class MongoMigrationConfiguration {

    @Value("${change-logs-scan-package}")
    private String changeLogsScanPackage;


    @Bean
    public SpringInitializingBeanRunner mongockInitializingBeanRunner(
            ApplicationContext springContext,
            MongoTemplate mongoTemplate) {
        return builder()
                .setDriver(SpringDataMongoV3Driver.withDefaultLock(mongoTemplate))
                .addChangeLogsScanPackage(changeLogsScanPackage)
                .setSpringContext(springContext)
                .dontFailIfCannotAcquireLock()
                .buildInitializingBeanRunner();
    }


}

package com.app;

import com.app.application.dto.CreateUserDto;
import com.app.infrastructure.repository.impl.UserRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CinemaApplication {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(CinemaApplication.class, args);
        var userRepositoryImpl = ctx.getBean("userRepositoryImpl", UserRepositoryImpl.class);

    }

}

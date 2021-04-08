package com.app;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.crypto.SecretKey;
import java.time.OffsetDateTime;
import java.util.concurrent.atomic.AtomicReference;


@SpringBootApplication
@EnableAspectJAutoProxy
public class CinemaApplication {

    public static final AtomicReference<OffsetDateTime> INSTANCE_CREATION_TIME = new AtomicReference<>(OffsetDateTime.now());

    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }

    @Bean
    public SecretKey secretKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    @Bean
    public JavaMailSender mailSender() {
        return new JavaMailSenderImpl();
    }


}


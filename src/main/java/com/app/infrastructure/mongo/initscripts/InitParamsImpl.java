package com.app.infrastructure.mongo.initscripts;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@EncryptablePropertySource("classpath:/encrypted.properties")
public class InitParamsImpl implements InitParams{

    @Value("${adminusername}")
    private String username;

    @Value("${encrypted.admin-password}")
    private String pass;

    @Override
    public String getAdminUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return pass;
    }
}

package com.app.infrastructure.mongo.initscripts;

import com.app.domain.security.Admin;
import com.app.domain.security.AdminRepository;
import com.app.infrastructure.mongo.initscripts.subscriber.AdminSubscriber;
import io.changock.migration.api.annotations.ChangeLog;
import io.changock.migration.api.annotations.ChangeSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@ChangeLog(order = "1")
@Slf4j
public class InitScripts {

    @ChangeSet(order = "001", id = "createAdmin", author = "CoderNoOne")
    public void createAdmin(AdminRepository adminRepository, InitParams initParams, PasswordEncoder passwordEncoder) {

        log.info("Executing script for adding default admin user");

        adminRepository
                .addOrUpdate(new Admin(initParams.getAdminUsername(), passwordEncoder.encode(initParams.getPassword())))
                .subscribeWith(new AdminSubscriber());
    }

}




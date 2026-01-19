package com.tms.finalproject_autoshop.actuator;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomInfoContributor implements InfoContributor {
    @Override
    public void contribute(Info.Builder builder) {
        builder
                .withDetail("app", Map.of(
                        "name", "Pet-project",
                        "description", "Auto Shop Management App",
                        "version", "1.0.0"
                ))
                .withDetail("contact", Map.of(
                        "email", "evgeniy.gorbenko2@gmail.com"
                ));
    }
}
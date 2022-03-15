package com.enel.platform.mepodlatam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import com.enel.platform.sdk.microservice.springboot.webfilter.PlatformRequestFilter;


@SpringBootApplication
@ServletComponentScan(PlatformRequestFilter.PACKAGE_LOCATION)
public class VirtualEntityApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(VirtualEntityApplication.class);
        application.run(args);
    }
}
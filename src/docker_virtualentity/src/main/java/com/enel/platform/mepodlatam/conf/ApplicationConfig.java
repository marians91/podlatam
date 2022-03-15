package com.enel.platform.mepodlatam.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.enel.platform.mepodlatam.controller.support.ControllerRequestInterceptor;


@Configuration
@ComponentScan(basePackages = { "com.enel.platform.mepodlatam" })
public class ApplicationConfig implements WebMvcConfigurer {

	@Bean
	public ControllerRequestInterceptor controllerRequestInterceptor() {
		return new ControllerRequestInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(controllerRequestInterceptor());
	}	

}

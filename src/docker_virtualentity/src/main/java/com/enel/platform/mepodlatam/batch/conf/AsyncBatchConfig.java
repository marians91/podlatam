package com.enel.platform.mepodlatam.batch.conf;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableAsync
@EnableScheduling
public class AsyncBatchConfig implements AsyncConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncBatchConfig.class); 
	
	@Override
	@Bean("taskExecutor")
	public AsyncTaskExecutor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(30);
		return executor;
	}

	@Bean
	public WebMvcConfigurer webMvcConfigurerConfigurer(AsyncTaskExecutor taskExecutor,
			CallableProcessingInterceptor callableProcessingInterceptor) {
		return new WebMvcConfigurer() {
			@Override
			public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
				configurer.setDefaultTimeout(360000).setTaskExecutor(taskExecutor);
				configurer.registerCallableInterceptors(callableProcessingInterceptor);				
				WebMvcConfigurer.super.configureAsyncSupport(configurer);
			}
		};
	}
	
	@Bean
    public CallableProcessingInterceptor callableProcessingInterceptor() {
        return new TimeoutCallableProcessingInterceptor() {
            @SuppressWarnings("hiding")
			@Override
            public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
            	LOGGER.warn("Task was in timeout");
                return super.handleTimeout(request, task);
            }
        };
    }
}

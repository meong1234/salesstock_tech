package com.ap.config.async;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.ap.config.base.FoundationProperties;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@Slf4j
public class SchedulingConfiguration implements SchedulingConfigurer {
	
	@Inject
    private FoundationProperties foundationProperties;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setTaskScheduler(taskScheduler());		
	}
	
	@Bean(name = "taskScheduler", destroyMethod="shutdown")
    public ThreadPoolTaskScheduler taskScheduler() {
		log.info("Creating Async taskScheduler");
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(foundationProperties.getAsync().getScheduledPoolSize());
        scheduler.setWaitForTasksToCompleteOnShutdown(false);

        return scheduler;
    }

}

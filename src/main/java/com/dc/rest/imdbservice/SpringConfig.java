package com.dc.rest.imdbservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

/***
 ** Author: Dominic Coutinho
 ** Description: Configuration to manage thread pooling for downloads
 */

@Configuration
@EnableAsync
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class SpringConfig {

  @Autowired
  private Environment environment;
  
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public ThreadPoolTaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
    int queueCapacity = Integer.parseInt(environment.getProperty("threadpoolexecutor.queueCapacity"));
    int corePoolSize = Integer.parseInt(environment.getProperty("threadpoolexecutor.corePoolSize"));
    int maxPoolSize = Integer.parseInt(environment.getProperty("threadpoolexecutor.maxPoolSize"));
    pool.setQueueCapacity(queueCapacity);
    pool.setCorePoolSize(corePoolSize);
    pool.setMaxPoolSize(maxPoolSize);
    pool.setWaitForTasksToCompleteOnShutdown(true);
    return pool;
  }
}

// End point to download files and unzip it in folder location
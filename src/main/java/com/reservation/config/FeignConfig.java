package com.reservation.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class FeignConfig {

//  @Value(value = "${mailgun.key}")
//  private String mailgunKey;

  @Qualifier(value = "mailgun")
  @Bean
  public BasicAuthRequestInterceptor basicAuthRequestInterceptor(){
    return new BasicAuthRequestInterceptor("api","134f7f536b270aeaf2f91505959f42e1-51356527-087aa56c");
  }



}

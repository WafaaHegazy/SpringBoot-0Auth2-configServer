package com.demo.feign.client;

import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;


public class TaskControllerFeignClientBuilder {
    private TaskResource taskResource = createClient(TaskResource.class, "http://localhost:8080/api");
    static AccountClientConfiguration accountClientConfiguration = new AccountClientConfiguration();

    public TaskResource getTaskResource() {
        return taskResource;
    }

    private static <T> T createClient(Class<T> type, String uri) {
        return Feign.builder()
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .requestInterceptor(accountClientConfiguration.oauth2FeignRequestInterceptor())
                .logger(new Slf4jLogger(type))
                .logLevel(Logger.Level.FULL)
                .target(type, uri);
    }
}
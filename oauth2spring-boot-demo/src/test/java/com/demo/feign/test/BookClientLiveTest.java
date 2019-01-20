package com.demo.feign.test;

import com.demo.feign.client.Task;
import com.demo.feign.client.TaskControllerFeignClientBuilder;
import com.demo.feign.client.TaskResource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

@Slf4j
@RunWith(JUnit4.class)
public class BookClientLiveTest {

    private TaskResource taskResource;

    @Before
    public void setup() {
        TaskControllerFeignClientBuilder feignClientBuilder = new TaskControllerFeignClientBuilder();
        taskResource = feignClientBuilder.getTaskResource();
    }
    @Test
    public void givenTaskClient_getAllTasks() throws Exception {
        taskResource.findAll();

    }

    @Test
    public void givenTaskClient_findtasl() throws Exception {
        taskResource.findOne(1l);

    }
}
package com.demo.feign.client;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;

import java.util.Arrays;

public class AccountClientConfiguration {
    @Bean
    RequestInterceptor oauth2FeignRequestInterceptor() {

        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), resource());

    }

    private OAuth2ProtectedResourceDetails resource() {

        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();

        resourceDetails.setAccessTokenUri("http://localhost:8080/oauth/token");

        resourceDetails.setClientId("devglan-client");

        resourceDetails.setClientSecret("devglan-secret");

        resourceDetails.setGrantType("client_credentials");

        resourceDetails.setScope(Arrays.asList("read"));

        return resourceDetails;

    }

}
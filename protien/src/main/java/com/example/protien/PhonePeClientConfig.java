package com.example.protien;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.phonepe.sdk.pg.Env;
import com.phonepe.sdk.pg.payments.v2.StandardCheckoutClient;

@Configuration
public class PhonePeClientConfig {

    @Value("${phonepe.client.id}")
    private String clientId;

    @Value("${phonepe.client.secret}")
    private String clientSecret;

    @Value("${phonepe.client.version:1}")
    private Integer clientVersion;

    @Value("${phonepe.environment:SANDBOX}")
    private String environment;

    @Bean
    public StandardCheckoutClient phonePeClient() {
        Env env = "PRODUCTION".equalsIgnoreCase(environment) ? Env.PRODUCTION : Env.SANDBOX;
        return StandardCheckoutClient.getInstance(clientId, clientSecret, clientVersion, env);
    }
}

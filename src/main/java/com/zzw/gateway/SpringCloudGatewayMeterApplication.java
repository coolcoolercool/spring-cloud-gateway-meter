package com.zzw.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.zzw.gateway.*"})
public class SpringCloudGatewayMeterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudGatewayMeterApplication.class, args);
    }

}

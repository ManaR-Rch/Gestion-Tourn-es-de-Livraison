package com.example.deliveryoptimizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import com.example.deliveryoptimizer.config.OpenApiConfig;

@SpringBootApplication
@Import(OpenApiConfig.class)
public class DeliveryOptimizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeliveryOptimizerApplication.class, args);
    }
}

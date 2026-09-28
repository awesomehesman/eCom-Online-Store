package com.enterprise.fashion.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class EnterpriseFashionEcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnterpriseFashionEcommerceApplication.class, args);
    }
}

package com.kutub.ecommerce.ecommerce_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${project.image}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // এই কনফিগারেশনটি আপনার 'images' ফোল্ডারকে /api/v1/images/ ইউআরএল-এ ম্যাপ করবে
        registry.addResourceHandler("/api/v1/images/**")
                .addResourceLocations("file:" + uploadDir);
    }
}

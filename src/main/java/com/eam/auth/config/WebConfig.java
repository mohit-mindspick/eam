package com.eam.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration to register interceptors for i18n functionality.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TenantInterceptor tenantInterceptor;
    private final LocaleInterceptor localeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Add locale interceptor first (order matters)
        registry.addInterceptor(localeInterceptor)
                .addPathPatterns("/api/**")
                .order(1);
        
        // Add tenant interceptor second
        registry.addInterceptor(tenantInterceptor)
                .addPathPatterns("/api/**")
                .order(2);
    }
}

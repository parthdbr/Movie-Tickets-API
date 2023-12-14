package com.movie.ticket.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    RoleBasedAccessInterceptor roleBasedAccessInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(roleBasedAccessInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/v3/api-docs/**, **/swagger-resources/**, /swagger-ui/**, /webjars/**", "/api/auth/**");
    }
}

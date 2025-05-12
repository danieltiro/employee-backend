package com.siscon.demo.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.siscon.demo.employee.utility.RequestHeadersInterceptor;

/**
 * Configuración para registrar interceptores en la aplicación.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final RequestHeadersInterceptor requestHeadersInterceptor;
    
    @Autowired
    public WebMvcConfig(RequestHeadersInterceptor requestHeadersInterceptor) {
        this.requestHeadersInterceptor = requestHeadersInterceptor;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestHeadersInterceptor)
                .addPathPatterns("/**");

        // Opcionalmente excluir ciertas rutas
        // .excludePathPatterns("/health", "/metrics", "/static/**");
    }
}
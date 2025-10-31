package com.unrobot_livehub.registro_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull; // <-- ¡AÑADE ESTA IMPORTACIÓN!
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) { // <-- ¡AÑADE @NonNull AQUÍ!
        registry.addMapping("/api/v1/**")  // Permite CORS para toda tu API
                .allowedOrigins("*") // El origen de tu app Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                .allowedHeaders("*")
                .allowCredentials(false);
    }
}
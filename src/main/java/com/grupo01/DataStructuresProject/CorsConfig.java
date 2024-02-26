package com.grupo01.DataStructuresProject;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc // Solo si no estás utilizando Spring Boot 2.0 o posterior
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:5173", "http://localhost:8080", "http://localhost:5500") // Reemplaza con tu dominio
            .allowedMethods("GET", "POST", "DELETE") // Métodos permitidos
            .allowCredentials(true) // Habilita el uso de credenciales (cookies)
            .maxAge(3600); // Tiempo máximo de caché de opciones preflight (en segundos)
    }
}

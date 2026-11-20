package com.friiadmi.config;

import com.friiadmi.service.FileStorageService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Publica los comprobantes Yape/Plin en /comprobantes/** (solo accesible con sesión de admin). */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final FileStorageService fileStorageService;

    public WebConfig(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String ubicacion = fileStorageService.getRaiz().toUri().toString();
        if (!ubicacion.endsWith("/")) {
            ubicacion = ubicacion + "/";
        }
        registry.addResourceHandler("/comprobantes/**").addResourceLocations(ubicacion);
    }
}

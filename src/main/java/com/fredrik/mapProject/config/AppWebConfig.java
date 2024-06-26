package com.fredrik.mapProject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class AppWebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/logout").setViewName("logout");
        registry.addViewController("/new-map").setViewName("new-map");
        registry.addViewController("/my-maps").setViewName("my-maps");
        registry.addViewController("/admin-page").setViewName("admin-page");
        registry.addViewController("/manage-map-players/**").setViewName("manage-map-players");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/resources/**")
                .addResourceLocations("classpath:/resources/");

        registry.addResourceHandler("/style/**")
                .addResourceLocations("classpath:/static/style/");

        registry.addResourceHandler("/script/**")
                .addResourceLocations("classpath:/static/script/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true)
                .maxAge(3600);
    }
}

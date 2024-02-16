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
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry register) {
        register.addResourceHandler("/resources/**", "/static/**")
                .addResourceLocations("/resources/", "classpath:/static/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}

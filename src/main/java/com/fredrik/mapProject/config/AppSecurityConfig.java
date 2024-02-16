package com.fredrik.mapProject.config;

import com.fredrik.mapProject.userDomain.service.UserEntityDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.util.concurrent.TimeUnit;

import static com.fredrik.mapProject.config.Roles.ADMIN;
import static com.fredrik.mapProject.config.Roles.USER;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AppSecurityConfig {

    private final AppPasswordConfig appPasswordConfig;
    private final UserEntityDetailsService userEntityDetailsService;

    @Autowired
    public AppSecurityConfig(AppPasswordConfig appPasswordConfig, UserEntityDetailsService userEntityDetailsService) {
        this.appPasswordConfig = appPasswordConfig;
        this.userEntityDetailsService = userEntityDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/",
                                "/login",
                                "/logout",
                                "/register",
                                "/myPerms").permitAll()
                        .requestMatchers(
                                "/new-map",
                                "/my-maps",
                                "/delete-map/**").hasRole(USER.name())
                        .requestMatchers(
                                "admin-page",
                                "edit-user"
                                ).hasRole(ADMIN.name())
                        .anyRequest().authenticated())

                .formLogin(formLogin -> formLogin.loginPage("/login"))   // Override /login

                .logout(logout -> logout
                        .logoutUrl("/perform_logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                        .logoutSuccessUrl("/login"))

                .rememberMe(rememberMe -> rememberMe
                        .tokenValiditySeconds(Math.toIntExact(TimeUnit.DAYS.toSeconds(21)))
                        .key("someSecureKey") // TODO: this is placeholder
                        .userDetailsService(userEntityDetailsService)
                        .rememberMeParameter("remember-me"))

                .authenticationProvider(daoAuthenticationProvider())    // Tell Spring to use our implementation (Password & Service)
                .build();
    }

    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(appPasswordConfig.bCryptPasswordEncoder());
        provider.setUserDetailsService(userEntityDetailsService);

        return provider;
    }


}

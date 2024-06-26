package com.fredrik.mapProject.config;

import com.fredrik.mapProject.service.user.UserEntityDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.concurrent.TimeUnit;

import static com.fredrik.mapProject.config.Roles.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AppSecurityConfig {

    private final AppPasswordConfig appPasswordConfig;
    private final UserEntityDetailsService userEntityDetailsService;
    private final JwtConfig jwtConfig;

    private final JwtRequestFilter jwtRequestFilter;

    @Value("${custom.token.key}")
    private String tokenKey;


    @Value("${map.app.url}")
    private String MAP_APP_URL;

    private final int EXPIRATION_TIME = Math.toIntExact(TimeUnit.DAYS.toSeconds(21));

    @Autowired
    public AppSecurityConfig(AppPasswordConfig appPasswordConfig, UserEntityDetailsService userEntityDetailsService, JwtConfig jwtConfig, JwtRequestFilter jwtRequestFilter) {
        this.appPasswordConfig = appPasswordConfig;
        this.userEntityDetailsService = userEntityDetailsService;
        this.jwtConfig = jwtConfig;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/login",
                                "/logout",
                                "/register",
                                "/").permitAll()
                        .requestMatchers(
                                "/api/**",
                                "/my-maps").hasAnyRole(USER.name(), HOST.name(), ADMIN.name())
                        .requestMatchers(
                                "/new-map",
                                "/manage-map-players/**",
                                "/delete-map/**").hasAnyRole(HOST.name(), ADMIN.name())
                        .requestMatchers(
                                "admin-page",
                                "edit-user"
                                ).hasRole(ADMIN.name()) // Only ADMIN role
                        .anyRequest().authenticated())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)

                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .successHandler(jwtAuthenticationSuccessHandler())
                )

                .logout(logout -> logout
                        .logoutUrl("/perform_logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                        .logoutSuccessUrl("/login"))

                .rememberMe(rememberMe -> rememberMe
                        .tokenValiditySeconds(EXPIRATION_TIME)
                        .key(tokenKey)
                        .userDetailsService(userEntityDetailsService)
                        .rememberMeParameter("remember-me"))

                .authenticationProvider(daoAuthenticationProvider())
                .build();
    }

    private AuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = generateJwtToken(userDetails.getUsername());

            response.sendRedirect(MAP_APP_URL + "/login?token=" + token);
        };
    }

    private String generateJwtToken(String username) {
        // Define token expiration time
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

        // Generate JWT token
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expirationDate)
                .signWith(jwtConfig.secretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(appPasswordConfig.bCryptPasswordEncoder());
        provider.setUserDetailsService(userEntityDetailsService);

        return provider;
    }
}

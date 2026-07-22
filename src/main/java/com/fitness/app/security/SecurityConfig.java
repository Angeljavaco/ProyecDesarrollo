package com.fitness.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;
import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter){
        this.jwtFilter = jwtFilter;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/reservas/mis-reservas").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.POST, "/api/reservas/**").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.DELETE, "/api/reservas/**").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.GET, "/api/reservas").hasAnyRole("ADMIN", "TRAINER")

                        .requestMatchers(HttpMethod.GET, "/api/clases/mis-clases").hasRole("TRAINER")
                        .requestMatchers(HttpMethod.POST, "/api/clases/**").hasAnyRole("TRAINER")
                        .requestMatchers(HttpMethod.PUT, "/api/clases/**").hasAnyRole("TRAINER")
                        .requestMatchers(HttpMethod.GET, "/api/reportes/**").hasRole("TRAINER")
                        .requestMatchers(HttpMethod.GET, "/api/clases/disponibles").hasAnyRole("ADMIN", "TRAINER", "MEMBER")
                        .requestMatchers(HttpMethod.GET, "/api/clases").hasAnyRole("ADMIN", "TRAINER", "MEMBER")

                        .requestMatchers(HttpMethod.DELETE, "/api/clases/**").hasRole("ADMIN")
                        .requestMatchers("/api/roles/**").hasRole("ADMIN")
                        .requestMatchers("/api/rol-usuario/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").hasRole("ADMIN")

                        .anyRequest().authenticated()
                );
        http.addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
        );
        return http.build();
    }
}
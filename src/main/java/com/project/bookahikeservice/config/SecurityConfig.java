package com.project.bookahikeservice.config;

import com.project.bookahikeservice.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomAccessDeniedHandler accessDeniedHandler,
            CustomAuthenticationEntryPoint authenticationEntryPoint
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**","/api/events/get-active", "/api/events/get-by-id").permitAll()
                        .requestMatchers("/api/events/get-by-id").permitAll()
                        .requestMatchers("/api/bookings/create-booking").permitAll()
                        .requestMatchers("/api/events/**").hasAnyRole("ADMIN", "ORGANIZER")
                        .requestMatchers("/api/events/create-event").hasAnyRole("ADMIN", "ORGANIZER")
                        .requestMatchers("/api/bookings/get-all-cancelled-booking").hasAnyRole("ADMIN", "ORGANIZER")
                        .requestMatchers("/api/bookings/get-all-active-booking").hasAnyRole("ADMIN", "ORGANIZER")
                        .requestMatchers("/api/bookings/get-all-past-booking").hasAnyRole("ADMIN", "ORGANIZER")
                        .requestMatchers("/api/bookings/delete-booking/**").hasAnyRole("ADMIN", "ORGANIZER")
                        .requestMatchers("/api/bookings/get-booking-event/**").hasAnyRole("ADMIN", "ORGANIZER")
                        .requestMatchers("/api/events/create-event").hasAnyRole("ADMIN", "ORGANIZER")
                        .requestMatchers("/api/joiner/**").hasRole("JOINER")
                        .requestMatchers("/api/organizer/**").hasRole("ORGANIZER")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/coordinator/**").hasRole("COORDINATOR")
                        .requestMatchers("/api/bookings/**").authenticated()
                        .requestMatchers("/api/eventBatch/**").authenticated()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

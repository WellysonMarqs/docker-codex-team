package com.customizationaudit.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@Profile("!local")
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/actuator/health/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                    JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
                    converter.setAuthorityPrefix("SCOPE_");
                    jwt.jwtAuthenticationConverter(token -> {
                        var authorities = converter.convert(token);
                        return new org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken(
                                token,
                                authorities
                        );
                    });
                }));
        return http.build();
    }
}

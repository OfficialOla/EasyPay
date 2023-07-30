package com.example.EasyPay.security;


import africa.semicolon.RealtyHub.security.filters.RegcrowAuthenticationFilter;
import africa.semicolon.RealtyHub.security.filters.RegcrowAuthorizationFilter;
import africa.semicolon.RealtyHub.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static africa.semicolon.RealtyHub.utils.AppUtils.LOGIN_ENDPOINT;
import static africa.semicolon.RealtyHub.utils.AppUtils.USER_API_VALUE;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      UsernamePasswordAuthenticationFilter authenticationFilter = new RegcrowAuthenticationFilter(authenticationManager, jwtUtil);
      authenticationFilter.setFilterProcessesUrl(LOGIN_ENDPOINT);
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(c->c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new RegcrowAuthorizationFilter(jwtUtil), RegcrowAuthenticationFilter.class)
                .addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(c->c.requestMatchers(POST, USER_API_VALUE)
                        .permitAll())
                .authorizeHttpRequests(c->c.requestMatchers(POST, LOGIN_ENDPOINT)
                        .permitAll())
                .authorizeHttpRequests(c->c.anyRequest().authenticated())
                .build();
    }
}

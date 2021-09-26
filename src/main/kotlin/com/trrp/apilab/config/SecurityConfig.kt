package com.trrp.apilab.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun configure(http: ServerHttpSecurity): SecurityWebFilterChain? {
        return http
            .authorizeExchange()
            .anyExchange()
            .authenticated()
            .and()
            .oauth2Login(Customizer.withDefaults())
            .csrf().disable()
            .build()
    }
}
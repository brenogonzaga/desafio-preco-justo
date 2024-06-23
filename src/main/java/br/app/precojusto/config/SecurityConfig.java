package br.app.precojusto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // TODO:  estabelecer configurações de segurança
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addExposedHeader("*");
        httpSecurity
            .cors(cors -> cors.configurationSource(request -> corsConfiguration))
            .csrf(csrf -> csrf.disable());
        return httpSecurity.build();
    }
    // @Bean
    // AuthenticationManager authenticationManager(
    //     AuthenticationConfiguration authConfig
    // )
    //     throws Exception {
    //     return authConfig.getAuthenticationManager();
    // }
}

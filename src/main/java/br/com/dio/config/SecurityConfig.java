package br.com.dio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(antMatcher("/public/**")).permitAll() // Permite acesso livre a /public
                        .anyRequest().authenticated() // Exige autenticação para qualquer outra rota
                )
                .formLogin(form -> {}) // Habilita login via formulário
                .httpBasic(httpBasic -> {}) // Habilita autenticação HTTP Basic
                .csrf(csrf -> csrf.disable()); // Desabilita CSRF para simplificação (opcional)

        return http.build();
    }
}
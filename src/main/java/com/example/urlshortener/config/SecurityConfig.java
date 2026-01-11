package com.example.urlshortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            // url별 권한 설정
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/admin/login").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().permitAll()
            )
            // 로그인 설정
            .formLogin(form -> form
                    .loginPage("/admin/login")
                    .defaultSuccessUrl("/admin", true)
                    .permitAll()
            )
            // 로그아웃 설정
            .logout(logout -> logout
                    .logoutUrl("/admin/logout")
                    .logoutSuccessUrl("/")
                    .permitAll()
            );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}1234") //테스트용
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}

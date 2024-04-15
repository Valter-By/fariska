package com.fufa.fariska.config;

import com.fufa.fariska.repository.UserRepository;
import com.fufa.fariska.service.GameUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

//    @Autowired
    public final UserRepository repository;

    @Bean
    public UserDetailsService userDetailsService() {
        return new GameUserDetailsService(repository);
    }
////        UserDetails admin = User.builder()
//    //                .username("valter")
//    //                .password(encoder.encode("321"))
//    //                .roles("ADMIN")
////                .build();
////        UserDetails user1 = User.builder()
////                .username("kate")
////                .password(encoder.encode("111"))
////                .roles("USER")
////                .build();
////        UserDetails user2 = User.builder()
////                .username("boris")
////                .password(encoder.encode("222"))
////                .roles("USER", "ADMIN")
////                .build();
////        return new InMemoryUserDetailsManager(admin, user1, user2);
//        return new GameUserDetailsService(userRepository);
//    }
//
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/game/**", "/game/join/**", "/hello", "/user/new", "/user/all", "/user/**").permitAll()
//                        .requestMatchers("/game/**").authenticated()
                        .requestMatchers("/admin").authenticated()
                        .requestMatchers("/admin").hasAnyRole("ADMIN")
                        )
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


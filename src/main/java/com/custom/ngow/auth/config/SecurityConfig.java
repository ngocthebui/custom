package com.custom.ngow.auth.config;

import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private static final String[] PUBLIC_API_LIST = {
      "/health",
      "/auth/**",
      "/accounts/create"
  };

  @Value("${jwt.signer_key}")
  private String signerKey;

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(PUBLIC_API_LIST).permitAll()
            .anyRequest().permitAll()
        );

    // verify token
    httpSecurity.oauth2ResourceServer(oauth2 ->
        oauth2.jwt(jwtConfigurer -> jwtConfigurer
                .decoder(jwtDecoder())
                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
            .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
    );

    httpSecurity.csrf(AbstractHttpConfigurer::disable);

    return httpSecurity.build();
  }

  @Bean
  JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
    converter.setAuthorityPrefix("ROLE_");

    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
    return jwtAuthenticationConverter;
  }

  @Bean
  JwtDecoder jwtDecoder() {
    SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
    return NimbusJwtDecoder
        .withSecretKey(secretKeySpec)
        .macAlgorithm(MacAlgorithm.HS512)
        .build();
  }

}

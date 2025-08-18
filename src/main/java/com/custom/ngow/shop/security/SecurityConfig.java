package com.custom.ngow.shop.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.custom.ngow.shop.config.CustomAuthenticationFailureHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  // 14 days
  private static final int REMEMBER_TOKEN_TIME = 1209600;

  private final UserDetailsService userDetailsService;
  private final DataSource dataSource;
  private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

  @Value("${jwt.signer_key}")
  private String secretKey;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            authz ->
                authz
                    .requestMatchers(
                        "/user/register", "/user/forgot-password", "/user/reset-password")
                    .permitAll()
                    .requestMatchers("/admin/**")
                    .hasAuthority("ADMIN")
                    .requestMatchers("/user/**")
                    .authenticated()
                    .anyRequest()
                    .permitAll())
        .formLogin(
            form ->
                form.loginPage("/login")
                    .loginProcessingUrl("/perform-login")
                    .defaultSuccessUrl("/", true)
                    .failureHandler(customAuthenticationFailureHandler)
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .permitAll())
        .logout(
            logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=true")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .permitAll())
        .sessionManagement(
            session ->
                session
                    .sessionCreationPolicy(
                        org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED)
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(false)
                    .expiredUrl("/login?expired=true"))
        .rememberMe(
            remember ->
                remember
                    .key(secretKey)
                    .tokenValiditySeconds(REMEMBER_TOKEN_TIME)
                    .userDetailsService(userDetailsService)
                    .tokenRepository(persistentTokenRepository())
                    .rememberMeParameter("remember-me")
                    .rememberMeCookieName("remember-me")
                    .useSecureCookie(false));

    return http.build();
  }

  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
    tokenRepository.setDataSource(dataSource);
    return tokenRepository;
  }
}

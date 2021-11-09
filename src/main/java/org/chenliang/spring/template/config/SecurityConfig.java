package org.chenliang.spring.template.config;

import org.chenliang.spring.template.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
  @Configuration
  @Order(1)
  public static class NoAuthApiConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
          .cors()
          .and()
          .csrf().disable()
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
          .requestMatchers()
          .antMatchers("/signup", "/login", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
          .and()
          .authorizeRequests()
          .anyRequest().anonymous();
    }
  }

  @Configuration
  @Order(2)
  public static class UserApiConfigurerAdapter extends WebSecurityConfigurerAdapter {
    private final AuthService authService;
    private final TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint;

    public UserApiConfigurerAdapter(AuthService authService, TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint) {
      this.authService = authService;
      this.tokenAuthenticationEntryPoint = tokenAuthenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      UserTokenAuthenticationFilter userTokenAuthenticationFilter = new UserTokenAuthenticationFilter(authService, tokenAuthenticationEntryPoint);
      http
          .cors()
          .and()
          .csrf().disable()
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
          .requestMatchers()
          .antMatchers("/**")
          .and()
          .authorizeRequests()
          .anyRequest().authenticated()
          .and()
          .addFilterBefore(userTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .maxAge(3600 * 24);
      }
    };
  }
}

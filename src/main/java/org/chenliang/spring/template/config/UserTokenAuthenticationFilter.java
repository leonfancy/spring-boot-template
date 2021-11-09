package org.chenliang.spring.template.config;

import lombok.extern.log4j.Log4j2;
import org.chenliang.spring.template.exception.TokenAuthenticationException;
import org.chenliang.spring.template.service.AuthService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Log4j2
public class UserTokenAuthenticationFilter extends OncePerRequestFilter {
  private final AuthService authService;

  private final TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint;

  public UserTokenAuthenticationFilter(AuthService authService, TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint) {
    this.authService = authService;
    this.tokenAuthenticationEntryPoint = tokenAuthenticationEntryPoint;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req,
                                  HttpServletResponse res,
                                  FilterChain chain) throws IOException, ServletException {
    String header = req.getHeader("Authorization");

    if (header == null || !header.startsWith(AuthService.TOKEN_PREFIX)) {
      chain.doFilter(req, res);
      return;
    }

    try {
      authService.authenticate(req).ifPresent(user -> {
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      });
    } catch (TokenAuthenticationException e) {
      tokenAuthenticationEntryPoint.commence(req, res, e);
      return;
    }

    chain.doFilter(req, res);
  }
}

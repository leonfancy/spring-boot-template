package org.chenliang.spring.template.service;

import org.chenliang.spring.template.exception.InvalidRequestException;
import org.chenliang.spring.template.exception.TokenAuthenticationException;
import org.chenliang.spring.template.model.dto.CreateUserRequest;
import org.chenliang.spring.template.model.dto.LoginRequest;
import org.chenliang.spring.template.model.dto.TokenResponse;
import org.chenliang.spring.template.model.entity.AccessToken;
import org.chenliang.spring.template.model.entity.User;
import org.chenliang.spring.template.repository.AccessTokenRepository;
import org.chenliang.spring.template.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class AuthService {
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final Duration TOKEN_EXPIRE_IN = Duration.ofDays(15); // 15 days

  private static final SecureRandom secureRandom = new SecureRandom();
  private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

  @Autowired
  private AccessTokenRepository accessTokenRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Transactional
  public Optional<User> authenticate(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                   .map(token -> token.replaceFirst(TOKEN_PREFIX, ""))
                   .map(token -> {
                     AccessToken accessToken =
                         accessTokenRepository.findByToken(token)
                                              .orElseThrow(() -> new TokenAuthenticationException("Token not found"));
                     if (accessToken.getExpireAt().isBefore(OffsetDateTime.now())) {
                       throw new TokenAuthenticationException("Expired token");
                     }

                     return accessToken;
                   })
                   .map(AccessToken::getUser);
  }

  @Transactional
  public TokenResponse register(@RequestBody @Valid CreateUserRequest request) {
    User user = userService.createUser(request);
    return createToken(user);
  }

  public TokenResponse login(LoginRequest request) {
    String email = request.getEmail();
    User user = userRepository.findByEmail(email)
                              .orElseThrow(() -> new InvalidRequestException("Invalid email or password"));
    if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      return createToken(user);
    } else {
      throw new InvalidRequestException("Invalid email or password");
    }
  }

  private TokenResponse createToken(User user) {
    String tokenString = randomToken();

    AccessToken accessToken = new AccessToken();
    accessToken.setToken(tokenString);
    accessToken.setExpireAt(OffsetDateTime.now().plus(TOKEN_EXPIRE_IN));
    accessToken.setUser(user);
    accessTokenRepository.save(accessToken);

    return TokenResponse.builder()
                        .accessToken(tokenString)
                        .expireIn((int) TOKEN_EXPIRE_IN.toSeconds())
                        .build();
  }

  private String randomToken() {
    byte[] randomBytes = new byte[48];
    secureRandom.nextBytes(randomBytes);
    return base64Encoder.encodeToString(randomBytes);
  }

}

package org.chenliang.spring.template.service;

import org.chenliang.spring.template.exception.InvalidRequestException;
import org.chenliang.spring.template.model.Role;
import org.chenliang.spring.template.model.dto.CreateUserRequest;
import org.chenliang.spring.template.model.dto.ResetPasswordRequest;
import org.chenliang.spring.template.model.dto.UpdateUserRequest;
import org.chenliang.spring.template.model.entity.User;
import org.chenliang.spring.template.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;

  private final BCryptPasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User createUser(CreateUserRequest request) {
    User user = new User();
    user.setRole(Role.ROLE_USER);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setEmail(request.getEmail());
    user.setName(request.getName());
    return userRepository.save(user);
  }

  public User updateCurrentUser(User tenant, UpdateUserRequest request) {
    tenant.setName(request.getName());
    tenant.setEmail(request.getEmail());
    return userRepository.save(tenant);
  }

  public void resetPassword(ResetPasswordRequest request, User user) {
    if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
      throw new InvalidRequestException("Invalid old password");
    }
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
  }
}

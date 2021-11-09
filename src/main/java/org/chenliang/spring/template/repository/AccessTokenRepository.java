package org.chenliang.spring.template.repository;

import org.chenliang.spring.template.model.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Integer> {
  Optional<AccessToken> findByToken(String token);
}

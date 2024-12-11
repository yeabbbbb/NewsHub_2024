package com.recommender.newshub.repository;

import com.recommender.newshub.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByLoginId(String loginId);

    Optional<User> findByLoginId(String loginId);
}

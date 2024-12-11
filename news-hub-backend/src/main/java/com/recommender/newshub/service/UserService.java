package com.recommender.newshub.service;

import com.recommender.newshub.domain.User;
import com.recommender.newshub.exception.ex.ConflictException;
import com.recommender.newshub.exception.ex.UnauthenticatedException;
import com.recommender.newshub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void signUp(User user) {
        checkLoginIdDuplicate(user.getLoginId());
        userRepository.save(user);
    }

    public User login(User loginForm) {
        return userRepository.findByLoginId(loginForm.getLoginId())
                .filter(user -> user.getPassword().equals(loginForm.getPassword()))
                .orElseThrow(() -> new UnauthenticatedException("Invalid ID or password"));
    }

    private void checkLoginIdDuplicate(String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            throw new ConflictException("This ID is already taken");
        }
    }
}

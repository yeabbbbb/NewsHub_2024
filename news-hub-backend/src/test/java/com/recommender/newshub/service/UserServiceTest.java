package com.recommender.newshub.service;

import com.recommender.newshub.domain.User;
import com.recommender.newshub.exception.ex.ConflictException;
import com.recommender.newshub.exception.ex.UnauthenticatedException;
import com.recommender.newshub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void signUp_O() {
        User user = new User("id", "pw");

        userService.signUp(user);

        User retrievedUser = userRepository.findByLoginId(user.getLoginId())
                .orElseThrow(() -> new AssertionError("User not found"));

        assertEquals(user.getLoginId(), retrievedUser.getLoginId());
        assertEquals(user.getPassword(), retrievedUser.getPassword());
    }

    @Test
    void signUp_X_DuplicatedId() {
        User existingUser = new User("id", "pw");
        userRepository.save(existingUser);

        User newUser = new User("id", "new");

        assertThrows(ConflictException.class, () -> userService.signUp(newUser));
    }

    @Test
    void login_O() {
        User user = new User("id", "pw");
        userRepository.save(user);

        User loggedInUser = new User("id", "pw");
        User foundUser = userService.login(loggedInUser);

        assertEquals(user.getLoginId(), foundUser.getLoginId());
        assertEquals(user.getPassword(), foundUser.getPassword());
    }

    @Test
    void login_X_InvalidCredentials() {
        User user = new User("id", "pw");

        assertThrows(UnauthenticatedException.class, () -> userService.login(user));
    }

}
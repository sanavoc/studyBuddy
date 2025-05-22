package com.esgi.studyBuddy.service;

import com.esgi.studyBuddy.model.User;
import com.esgi.studyBuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UUID createUser(User user) {
        return userRepository.save(user).getId();
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}

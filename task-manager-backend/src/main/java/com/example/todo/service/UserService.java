package com.example.todo.service;

import com.example.todo.User;
import com.example.todo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String username) {
        User user = new User(UUID.randomUUID().toString(), username);
        return userRepository.save(user);
    }

    public User getUser(String userId) {
        return userRepository.findById(userId);
    }

    public User addXp(String userId, int xpToAdd) {
        User user = userRepository.findById(userId);
        if (user != null) {
            int currentXp = user.getXp();
            int newXp = currentXp + xpToAdd;
            int newLevel = calculateLevel(newXp);

            user.setXp(newXp);
            user.setLevel(newLevel);
            user.setTasksCompleted(user.getTasksCompleted() + 1);

            return userRepository.update(user);
        }
        return null;
    }

    private int calculateLevel(int xp) {
        return (int) Math.sqrt(xp / 100) + 1;
    }
}
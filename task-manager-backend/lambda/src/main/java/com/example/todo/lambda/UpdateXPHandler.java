package com.example.todo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.todo.lambda.model.User;
import com.example.todo.lambda.repository.UserRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class UpdateXPHandler implements RequestHandler<Object, String> {
    private final UserRepository userRepository;
    private static final int XP_PER_DAY = 10;

    public UpdateXPHandler() {
        this.userRepository = new UserRepository();
    }

    @Override
    public String handleRequest(Object input, Context context) {
        List<User> users = userRepository.getAllUsers();
        Instant now = Instant.now();

        for (User user : users) {
            Instant lastUpdated = user.getLastUpdated();
            if (lastUpdated != null) {
                Duration timeSinceLastUpdate = Duration.between(lastUpdated, now);
                long daysElapsed = timeSinceLastUpdate.toDays();

                if (daysElapsed > 0) {
                    int xpToAdd = (int) (daysElapsed * XP_PER_DAY);
                    user.setXp(user.getXp() + xpToAdd);
                    user.setLastUpdated(now);
                    userRepository.updateUser(user);
                }
            }
        }

        return "XP update completed successfully";
    }
}
package com.example.todo.controller;

import com.example.todo.User;
import com.example.todo.service.TodoService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final TodoService todoService;

    public UserController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/stats")
    public Map<String, Object> getUserStats() {
        // For now, we'll use a hardcoded user ID
        String userId = "default-user";

        int xp = todoService.getUserXp(userId);
        int level = todoService.getUserLevel(xp);
        int tasksCompleted = todoService.getCompletedTaskCount(userId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("xp", xp);
        stats.put("level", level);
        stats.put("tasksCompleted", tasksCompleted);

        return stats;
    }
}
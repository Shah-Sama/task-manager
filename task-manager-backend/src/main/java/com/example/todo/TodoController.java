package com.example.todo;

import com.example.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:3000")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo, @RequestParam String userId) {
        return todoService.createTodo(todo.getTitle(), todo.getDescription(), userId);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable String id) {
        todoService.deleteTodo(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable String id, @RequestBody Todo todo) {
        Todo updatedTodo = todoService.updateTodo(id, todo.getTitle(), todo.getDescription(), todo.isCompleted());
        if (updatedTodo != null) {
            return ResponseEntity.ok(updatedTodo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<Todo> toggleTodo(@PathVariable String id, @RequestParam String userId) {
        Todo updatedTodo = todoService.toggleTodo(id, userId);
        if (updatedTodo != null) {
            return ResponseEntity.ok(updatedTodo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserStats(@PathVariable String userId) {
        int totalXp = todoService.getUserXp(userId);
        int level = todoService.getUserLevel(totalXp);
        int tasksCompleted = todoService.getCompletedTaskCount(userId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("xp", totalXp);
        stats.put("level", level);
        stats.put("tasksCompleted", tasksCompleted);

        return ResponseEntity.ok(stats);
    }
}
package com.example.todo;

import com.example.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.createTodo(todo.getTitle(), todo.getDescription());
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
    public ResponseEntity<Todo> toggleTodo(@PathVariable String id) {
        Todo updatedTodo = todoService.toggleTodo(id);
        if (updatedTodo != null) {
            return ResponseEntity.ok(updatedTodo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
package com.example.todo.service;

import com.example.todo.Todo;
import com.example.todo.TodoRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Todo createTodo(String title, String description) {
        Todo todo = new Todo(
                UUID.randomUUID().toString(),
                title,
                description,
                false,
                Instant.now().toString());
        return todoRepository.save(todo);
    }

    public Todo updateTodo(String id, String title, String description, boolean completed) {
        Todo todo = todoRepository.findById(id);
        if (todo != null) {
            todo.setTitle(title);
            todo.setDescription(description);
            todo.setCompleted(completed);
            return todoRepository.update(todo);
        }
        return null;
    }

    public void deleteTodo(String id) {
        todoRepository.deleteById(id);
    }

    public Todo toggleTodo(String id) {
        Todo todo = todoRepository.findById(id);
        if (todo != null) {
            todo.setCompleted(!todo.isCompleted());
            return todoRepository.update(todo);
        }
        return null;
    }
}
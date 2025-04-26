package com.example.todo;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TodoRepository {
    private final ConcurrentHashMap<String, Todo> todos = new ConcurrentHashMap<>();

    public Todo save(Todo todo) {
        if (todo.getId() == null) {
            todo.setId(UUID.randomUUID().toString());
        }
        todos.put(todo.getId(), todo);
        return todo;
    }

    public List<Todo> findAll() {
        return new ArrayList<>(todos.values());
    }

    public void deleteById(String id) {
        todos.remove(id);
    }

    public Todo findById(String id) {
        return todos.get(id);
    }
}
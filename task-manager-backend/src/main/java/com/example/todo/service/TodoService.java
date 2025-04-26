package com.example.todo.service;

import com.example.todo.Todo;
import com.example.todo.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public void deleteTodo(String id) {
        todoRepository.deleteById(id);
    }

    public Todo updateTodo(String id, Todo todo) {
        Todo existingTodo = todoRepository.findById(id);
        if (existingTodo != null) {
            existingTodo.setText(todo.getText());
            existingTodo.setCompleted(todo.isCompleted());
            return todoRepository.save(existingTodo);
        }
        return null;
    }
}
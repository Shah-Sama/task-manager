package com.example.todo.service;

import com.example.todo.Todo;
import com.example.todo.TodoRepository;
import com.example.todo.User;
import com.example.todo.UserRepository;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final SqsClient sqsClient;
    private static final int XP_PER_TASK = 10;
    private static final String XP_UPDATE_QUEUE_URL = "https://sqs.us-east-1.amazonaws.com/791267371700/task-manager-xp-updates";

    public TodoService(TodoRepository todoRepository, UserRepository userRepository, SqsClient sqsClient) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
        this.sqsClient = sqsClient;
    }

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Todo createTodo(String title, String description, String userId) {
        Todo todo = new Todo(
                UUID.randomUUID().toString(),
                title,
                description,
                false,
                Instant.now().toString());
        todo.setUserId(userId);
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

    public Todo toggleTodo(String id, String userId) {
        Todo todo = todoRepository.findById(id);
        if (todo != null) {
            boolean newCompleted = !todo.isCompleted();
            todo.setCompleted(newCompleted);

            if (newCompleted) {
                // Send message to SQS to trigger XP update
                SendMessageRequest messageRequest = SendMessageRequest.builder()
                        .queueUrl(XP_UPDATE_QUEUE_URL)
                        .messageBody(userId)
                        .build();
                sqsClient.sendMessage(messageRequest);
            }

            return todoRepository.update(todo);
        }
        return null;
    }

    public int getUserXp(String userId) {
        User user = userRepository.findById(userId);
        return user != null ? user.getXp() : 0;
    }

    public int getUserLevel(int xp) {
        return (int) Math.sqrt(xp / 100) + 1;
    }

    public int getCompletedTaskCount(String userId) {
        User user = userRepository.findById(userId);
        return user != null ? user.getTasksCompleted() : 0;
    }
}
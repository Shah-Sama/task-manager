package com.example.todo.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.example.todo.Todo;
import com.example.todo.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private AmazonSQS amazonSQS;

    @Autowired
    private AmazonSNS amazonSNS;

    @Value("${aws.sqs.todo-queue-url}")
    private String todoQueueUrl;

    @Value("${aws.sns.todo-topic-arn}")
    private String todoTopicArn;

    public Todo createTodo(Todo todo) {
        Todo savedTodo = todoRepository.save(todo);

        // Send message to SQS for async processing
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(todoQueueUrl)
                .withMessageBody("New todo created: " + todo.getText());
        amazonSQS.sendMessage(sendMessageRequest);

        // Publish notification to SNS
        amazonSNS.publish(todoTopicArn, "New todo created: " + todo.getText());

        return savedTodo;
    }

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public void deleteTodo(String id) {
        Todo todo = todoRepository.findById(id);
        if (todo != null) {
            todoRepository.deleteById(id);

            // Send message to SQS for async processing
            SendMessageRequest sendMessageRequest = new SendMessageRequest()
                    .withQueueUrl(todoQueueUrl)
                    .withMessageBody("Todo deleted: " + todo.getText());
            amazonSQS.sendMessage(sendMessageRequest);
        }
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
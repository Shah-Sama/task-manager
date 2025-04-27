package com.example.todo;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class TodoRepository {
    private final DynamoDbTable<Todo> todoTable;

    public TodoRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.todoTable = dynamoDbEnhancedClient.table("Todos", TableSchema.fromBean(Todo.class));
    }

    public Todo save(Todo todo) {
        if (todo.getId() == null) {
            todo.setId(UUID.randomUUID().toString());
        }
        todoTable.putItem(todo);
        return todo;
    }

    public List<Todo> findAll() {
        return todoTable.scan().items().stream().collect(Collectors.toList());
    }

    public Todo findById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return todoTable.getItem(key);
    }

    public void deleteById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        todoTable.deleteItem(key);
    }

    public Todo update(Todo todo) {
        return save(todo);
    }
}
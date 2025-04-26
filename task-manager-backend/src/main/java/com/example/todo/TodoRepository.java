package com.example.todo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class TodoRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Todo save(Todo todo) {
        if (todo.getId() == null) {
            todo.setId(UUID.randomUUID().toString());
        }
        dynamoDBMapper.save(todo);
        return todo;
    }

    public List<Todo> findAll() {
        return dynamoDBMapper.scan(Todo.class, new DynamoDBScanExpression());
    }

    public void deleteById(String id) {
        Todo todo = new Todo();
        todo.setId(id);
        dynamoDBMapper.delete(todo);
    }

    public Todo findById(String id) {
        return dynamoDBMapper.load(Todo.class, id);
    }
}
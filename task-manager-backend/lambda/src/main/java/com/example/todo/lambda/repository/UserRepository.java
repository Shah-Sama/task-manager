package com.example.todo.lambda.repository;

import com.example.todo.lambda.model.User;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.stream.Collectors;

public class UserRepository {
    private final DynamoDbTable<User> userTable;

    public UserRepository() {
        DynamoDbClient ddb = DynamoDbClient.builder().build();
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddb)
                .build();

        this.userTable = enhancedClient.table("Users", TableSchema.fromBean(User.class));
    }

    public List<User> getAllUsers() {
        return userTable.scan().items().stream().collect(Collectors.toList());
    }

    public void updateUser(User user) {
        userTable.updateItem(user);
    }
}
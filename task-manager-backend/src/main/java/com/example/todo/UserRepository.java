package com.example.todo;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final DynamoDbTable<User> userTable;

    public UserRepository(DynamoDbEnhancedClient enhancedClient) {
        this.userTable = enhancedClient.table("Users", TableSchema.fromBean(User.class));
    }

    public User findById(String userId) {
        return userTable.getItem(r -> r.key(k -> k.partitionValue(userId)));
    }

    public User save(User user) {
        userTable.putItem(user);
        return user;
    }

    public User update(User user) {
        return save(user);
    }
}
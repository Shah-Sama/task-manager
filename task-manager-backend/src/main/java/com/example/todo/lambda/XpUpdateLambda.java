package com.example.todo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.example.todo.User;
import com.example.todo.UserRepository;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class XpUpdateLambda implements RequestHandler<SQSEvent, Void> {
    private final UserRepository userRepository;
    private static final int XP_PER_TASK = 10;

    public XpUpdateLambda() {
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        this.userRepository = new UserRepository(enhancedClient);
    }

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        context.getLogger().log("Starting XP update process");

        for (SQSEvent.SQSMessage message : event.getRecords()) {
            String userId = message.getBody();
            context.getLogger().log("Processing XP update for user: " + userId);

            try {
                User user = userRepository.findById(userId);

                if (user != null) {
                    int currentXp = user.getXp();
                    int newXp = currentXp + XP_PER_TASK;
                    int newLevel = (int) Math.sqrt(newXp / 100) + 1;

                    user.setXp(newXp);
                    user.setLevel(newLevel);
                    user.setTasksCompleted(user.getTasksCompleted() + 1);

                    userRepository.update(user);

                    context.getLogger().log(String.format(
                            "Updated XP for user %s: XP=%d, Level=%d, Tasks=%d",
                            userId, newXp, newLevel, user.getTasksCompleted()));
                } else {
                    context.getLogger().log("User not found: " + userId);
                }
            } catch (Exception e) {
                context.getLogger().log("Error processing user " + userId + ": " + e.getMessage());
                throw e; // Re-throw to ensure the message goes back to the queue
            }
        }

        context.getLogger().log("XP update process completed");
        return null;
    }
}
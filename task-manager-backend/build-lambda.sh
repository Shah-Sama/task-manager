#!/bin/bash

# Create a temporary directory
mkdir -p target/lambda

# Compile the Lambda function with all dependencies
javac -cp "$(find ~/.m2 -name '*.jar' | tr '\n' ':')" \
    src/main/java/com/example/todo/lambda/XpUpdateLambda.java \
    src/main/java/com/example/todo/User.java \
    src/main/java/com/example/todo/UserRepository.java

# Create the JAR file with all dependencies
jar cvf target/lambda/xp-update-lambda.jar \
    com/example/todo/lambda/XpUpdateLambda.class \
    com/example/todo/User.class \
    com/example/todo/UserRepository.class \
    $(find ~/.m2 -name 'aws-lambda-java-core-*.jar' -o -name 'aws-lambda-java-events-*.jar' -o -name 'aws-java-sdk-core-*.jar' -o -name 'aws-java-sdk-sqs-*.jar' -o -name 'aws-java-sdk-dynamodb-*.jar')

# Clean up
rm -rf com 
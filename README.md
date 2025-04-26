# Task Manager Application

A modern task management application built with React and Spring Boot, utilizing AWS services for scalability and reliability.

## Features

- Create, read, update, and delete tasks
- Mark tasks as complete/incomplete
- Modern Material-UI interface
- AWS integration (DynamoDB, SQS, SNS)
- Real-time notifications
- Asynchronous task processing

## Tech Stack

### Frontend

- React
- Material-UI
- AWS SDK for JavaScript

### Backend

- Spring Boot
- AWS DynamoDB
- AWS SQS
- AWS SNS
- AWS Lambda

## Getting Started

### Prerequisites

- Node.js (v14 or higher)
- Java 11 or higher
- Maven
- AWS Account with appropriate permissions

### Installation

1. Clone the repository:

```bash
git clone https://github.com/Shah-Sama/task-manager.git
cd task-manager
```

2. Configure AWS credentials in `task-manager-backend/src/main/resources/application.properties`

3. Start the backend:

```bash
cd task-manager-backend
mvn spring-boot:run
```

4. Start the frontend:

```bash
cd task-manager-frontend
npm install
npm start
```

The application will be available at:

- Frontend: http://localhost:3000
- Backend: http://localhost:8080

## AWS Services Configuration

1. Create a DynamoDB table named "Todos"
2. Create an SQS queue for async operations
3. Create an SNS topic for notifications
4. Update the AWS credentials in application.properties

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License.

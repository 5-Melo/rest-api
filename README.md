# MeloTask - Task Management System
MeloTask is a **RESTful task management system** built with **Spring Boot** in **MVC design pattern**, designed to help teams efficiently organize and track their work. Inspired by tools like **Jira**, it allows users to manage projects, create tasks, and collaborate seamlessly.
With features like **JWT authentication, OAuth2 via Google, and MongoDB Atlas for data storage**, MeloTask ensures a **secure**, **scalable**, and **flexible** experience for managing tasks. The API is well-documented using **Swagger**, and the project is fully containerized with **Docker** for easy deployment.

## 📖 API Documentation

MeloTask provides a comprehensive and interactive API documentation using **Swagger UI**. This allows developers to explore and test API endpoints with ease.

### 🔹 Accessing the API Docs
Once the server is running, open your browser and navigate to:

👉 **[Swagger UI](http://localhost:8080/swagger-ui/index.html#)**

### 📷 API photo example
![image](https://github.com/user-attachments/assets/19fa75c0-d531-45b4-9d68-0f82d78fa246)

## 🔐 Authentication & Authorization

MeloTask implements **JWT-based authentication** to ensure secure access to the API. Users have two options to authenticate:

1. **Login with Username & Password**  
2. **Login via Google OAuth2**

Regardless of the method, upon successful authentication, a **unique JWT (JSON Web Token)** is generated for the user.

### 🔹 JWT Token Structure  
The JWT contains important user information and is signed for security. Below is an example of the token payload:

```json
{
  "userId": "67be06b24fg00c4dce2e43d7",
  "sub": "Ahmed123", 
  "iat": 1740862387, 
  "exp": 1740948787 
}
```
## 🐳 Project Setup with Docker

MeloTask is fully containerized using **Docker**, making it easy to set up and run with minimal configuration.

### 🔹 Prerequisites
Ensure you have **Docker** and **Docker Compose** installed on your system.

### 🔹 Step 1: Create a `.env` File
In the project root directory, create a `.env` file and add the required environment variables:

```env
MONGO_URI=
MONGO_DATABASE=
JWT_SECRET_KEY=
JWT_EXPIRATION_TIME=
GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=
```
### 🔹 Step 2: Run the Application with Docker
To build and start the application, run:
```sh
docker-compose up --build
```
This will:
+ Pull the necessary dependencies
+ Build the Docker image
+ Build the JAR file into the docker container
+ Start the MeloTask server

package com.MeloTech.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) {
        ProblemDetail errorDetail = null;

        // TODO send this stack trace to an observability tool
        exception.printStackTrace();

        switch (exception) {
            case UserNotFoundException userNotFoundException -> {
                errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), exception.getMessage());
                errorDetail.setProperty("description", "The requested user was not found");
                return errorDetail;
            }
            case UsernameAlreadyTakenException usernameAlreadyTakenException -> {
                errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(409), exception.getMessage());
                errorDetail.setProperty("description", "Username is already taken");
                return errorDetail;
            }
            case EmailAlreadyExistException emailAlreadyExistException -> {
                errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(409), exception.getMessage());
                errorDetail.setProperty("description", "Email already Exists");
                return errorDetail;
            }
            case BadCredentialsException badCredentialsException -> {
                errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
                errorDetail.setProperty("description", "The username or password is incorrect");

                return errorDetail;
            }
            case null, default -> {
            }
        }

        return errorDetail;
    }
}
package com.MeloTech.handlers;

import com.MeloTech.exceptions.EmailAlreadyExistException;
import com.MeloTech.exceptions.UserNotFoundException;
import com.MeloTech.exceptions.UsernameAlreadyTakenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) {
        ProblemDetail errorDetail;

        // TODO: Send this stack trace to an observability tool
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
                errorDetail.setProperty("description", "Email already exists");
                return errorDetail;
            }
            case BadCredentialsException badCredentialsException -> {
                errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
                errorDetail.setProperty("description", "The username or password is incorrect");
                return errorDetail;
            }
            case AccountStatusException accountStatusException -> {
                errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
                errorDetail.setProperty("description", "The account is locked");
                return errorDetail;
            }
            case AccessDeniedException accessDeniedException -> {
                errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
                errorDetail.setProperty("description", "You are not authorized to access this resource");
                return errorDetail;
            }
            case SignatureException signatureException -> {
                errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
                errorDetail.setProperty("description", "The JWT signature is invalid");
                return errorDetail;
            }
            case ExpiredJwtException expiredJwtException -> {
                errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
                errorDetail.setProperty("description", "The JWT token has expired");
                return errorDetail;
            }
            default -> {
                errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage());
                errorDetail.setProperty("description", "Unknown internal server error.");
                return errorDetail;
            }
        }
    }
}

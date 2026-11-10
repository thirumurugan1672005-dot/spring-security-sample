package com.example.sample.exception;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

     @ExceptionHandler(DuplicateEmailException.class)
     public ProblemDetail handleDuplicateEmail(DuplicateEmailException e) {
          ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
          detail.setTitle("Duplicate Email Error");
          detail.setDetail("Email already exists");
          return detail;
     }

     @ExceptionHandler(PasswordTooLongException.class)
     public ProblemDetail handlePasswordLength(PasswordTooLongException e) {
          ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
          detail.setTitle("Password Length error");
          detail.setDetail("Password was too long");
          return detail;
     }
  
     @ExceptionHandler(MethodArgumentNotValidException.class)
     public ProblemDetail handleNotValid(MethodArgumentNotValidException e) {
          ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
          detail.setTitle("Invalid Details");
          detail.setDetail("Invalid data provided");
          Map<String, List<String>> errors = e.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.groupingBy(
                              FieldError::getField,
                              Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));

          detail.setProperty("errors", errors);
          return detail;
     }

     @ExceptionHandler (BadCredentialsException.class)
     public ProblemDetail handleBadCredentailsException(BadCredentialsException e){
           ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
           detail.setTitle("Wrong credentials");
           detail.setDetail("Incorrect email or password");
           return detail;
     }
}

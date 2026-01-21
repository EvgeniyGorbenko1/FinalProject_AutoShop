package com.tms.finalproject_autoshop.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(value = {UsernameUsedException.class})
    public ResponseEntity<HttpStatusCode> usernameUsedException(UsernameUsedException e) {
        log.info("UsernameUsedException" + e.getUsername());
        return new ResponseEntity<>(HttpStatus.IM_USED);
    }

    @ExceptionHandler(value = EmailException.class)
    public ResponseEntity<HttpStatusCode> emailException(EmailException e) {
        log.info("EmailException" + e.getMessage());
        return new ResponseEntity<>(HttpStatus.IM_USED);
    }
}

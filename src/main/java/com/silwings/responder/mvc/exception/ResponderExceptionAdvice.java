package com.silwings.responder.mvc.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ResponderExceptionAdvice {

    @ExceptionHandler(ResponderException.class)
    public ResponseEntity<String> handleLyException(ResponderException e) {
        log.error("ResponderExceptionAdvice: 捕获异常信息.", e);
        return ResponseEntity.status(500).body(e.getMessage());
    }

}
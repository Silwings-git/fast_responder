package com.silwings.responder.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleLyException(Exception e) {
        log.error("统一异常处理捕获异常信息.", e);
        return ResponseEntity.status(500).body(e.getMessage());
    }

}
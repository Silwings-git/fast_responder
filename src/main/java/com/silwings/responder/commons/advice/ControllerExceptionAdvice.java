package com.silwings.responder.commons.advice;

import com.silwings.db.db_editor.bean.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @ClassName ControllerExceptionAdvice
 * @Description ControllerExceptionAdvice
 * @Author 崔益翔
 * @Date 2020/3/12 21:22
 * @Version V1.0
 **/
@Slf4j
@ControllerAdvice
public class ControllerExceptionAdvice {
    /**
     * 统一异常处理方法，@ExceptionHandler(LyException.class)声明这个方法处理LyException这样的异常
     *
     * @param e 捕获到的异常
     * @return 返回给页面的状态码和信息
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleLyException(Exception e) {
        log.error("统一异常处理捕获异常信息 : {}", e.getMessage());
        return ResponseEntity.status(200).body(Result.failed(e.getMessage()));
    }

}
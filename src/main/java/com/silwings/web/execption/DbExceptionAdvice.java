package com.silwings.web.execption;

import com.silwings.web.bean.result.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @ClassName DbExceptionAdvice
 * @Description DbExceptionAdvice
 * @Author Silwings
 * @Date 2021/8/8 13:28
 * @Version V1.0
 **/
@Slf4j
@ControllerAdvice
public class DbExceptionAdvice {

    @ExceptionHandler(DbException.class)
    public ResponseEntity<WebResult<Void>> handleLyException(final DbException e) {
        log.error("DbExceptionAdvice 捕获异常信息.", e);
        return ResponseEntity.ok(WebResult.fail(e.getMessage()));
    }

}
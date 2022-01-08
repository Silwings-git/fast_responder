package com.silwings.db.controller.execption;

import com.silwings.db.controller.bean.result.ResponderResult;
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
    public ResponseEntity<ResponderResult<Void>> handleLyException(final DbException e) {
        log.error("统一异常处理捕获异常信息.", e);
        return ResponseEntity.ok(ResponderResult.fail(e.getMessage()));
    }

}
package com.onions.timer.handle;

import com.onions.timer.exception.ParamsInvalidException;
import com.onions.timer.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.UnsupportedEncodingException;

@RestControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity handleNotFound() {
        ErrorResponse errorResponse = new ErrorResponse(404, "路由不存在");
        return new ResponseEntity<Object>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleBindException(BindException e) {
        BindingResult result = e.getBindingResult();
        return handleException(result);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleRequestBodyInvalid(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        return handleException(result);
    }

    private ResponseEntity handleException(BindingResult result) {
        FieldError fieldError = result.getFieldError();
        String field = fieldError.getField();
        ErrorResponse errorResponse = new ErrorResponse(400, field + "参数错误");
        return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("请求body格式错误: ", e);
        ErrorResponse errorResponse = new ErrorResponse(1003, "请求body格式错误");
        return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParamsInvalidException.class)
    public ResponseEntity handleCommonException(ParamsInvalidException e) {
        log.warn("参数校验错误: ", e);
        ErrorResponse errorResponse = new ErrorResponse(e.getCode(), e.getMessage());
        return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("不支持的请求方法: ", e);
        ErrorResponse errorResponse = new ErrorResponse(1004, "不支持的请求方法");
        return new ResponseEntity<Object>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        log.error("系统错误: ", e);
        ErrorResponse errorResponse = new ErrorResponse(1005, "系统繁忙");
        return new ResponseEntity<Object>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

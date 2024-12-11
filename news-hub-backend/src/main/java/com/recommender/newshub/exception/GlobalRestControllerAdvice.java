package com.recommender.newshub.exception;

import com.recommender.newshub.web.dto.common.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalRestControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleExceptionInternal(ex, headers, HttpStatus.UNAUTHORIZED, request, null);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleExceptionInternal(ex, headers, HttpStatus.BAD_REQUEST, request, null);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = extractFieldErrors(ex);
        return handleExceptionInternal(ex, headers, HttpStatus.BAD_REQUEST, request, errors);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Object[] args = {ex.getPropertyName(), ex.getValue()};
        String defaultDetail = "Failed to convert '" + args[0] + "' with value: '" + args[1] + "'";
        return handleExceptionInternal(ex, headers, HttpStatus.BAD_REQUEST, request, defaultDetail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ConstraintViolationException 추출 도중 에러 발생"));
        return handleExceptionInternal(ex, HttpHeaders.EMPTY, HttpStatus.BAD_REQUEST, request, errorMessage);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomEx(CustomException ex, WebRequest request) {
        return handleExceptionInternal(ex, HttpHeaders.EMPTY, ex.getStatus(), request, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleEx(Exception ex, WebRequest request) {
        log.error("ex: ", ex);
        return handleExceptionInternal(ex, HttpHeaders.EMPTY, HttpStatus.INTERNAL_SERVER_ERROR, request, null);
    }

    private <T> ResponseEntity<Object> handleExceptionInternal(Exception ex, HttpHeaders headers, HttpStatus status, WebRequest request, T data) {
        ApiResponse<T> body = ApiResponse.onFailure(status, ex.getMessage(), data);
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private static Map<String, String> extractFieldErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(fieldError -> {
                    String fieldName = StringUtils.uncapitalize(
                            fieldError.getField().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase()
                    );
                    String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
                    errors.merge(fieldName, errorMessage,
                            (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
                });

        return errors;
    }
}

package com.recommender.newshub.exception.ex;

import com.recommender.newshub.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ConflictException extends CustomException {
    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}

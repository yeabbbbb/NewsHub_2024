package com.recommender.newshub.exception.ex;

import com.recommender.newshub.exception.CustomException;
import org.springframework.http.HttpStatus;

public class PageException extends CustomException {
    public PageException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

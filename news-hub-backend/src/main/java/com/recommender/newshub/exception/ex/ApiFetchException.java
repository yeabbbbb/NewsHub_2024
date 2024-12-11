package com.recommender.newshub.exception.ex;

import com.recommender.newshub.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ApiFetchException extends CustomException {
    public ApiFetchException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}

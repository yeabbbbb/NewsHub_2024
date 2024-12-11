package com.recommender.newshub.web.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"success", "code", "message", "result"})
public class ApiResponse<T> {

    private final Boolean success;
    private final int code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public static <T> ApiResponse<T> onSuccess(HttpStatus status, T result) {
        return new ApiResponse<>(true, status.value(), status.getReasonPhrase(), result);
    }

    public static <T> ApiResponse<T> onFailure(HttpStatus status, String message, T result) {
        return new ApiResponse<>(false, status.value(), message, result);
    }
}

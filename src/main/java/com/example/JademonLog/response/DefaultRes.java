package com.example.JademonLog.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefaultRes<T> {
    private HttpStatus httpStatus;
    private Integer statusCode;
    private String responseMessage;
    private T result;

    public DefaultRes(final HttpStatus httpStatus, final int statusCode, final String responseMessage) {
        this.httpStatus = httpStatus;
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
        this.result = null;
    }

    public static<T> DefaultRes<T> res(final HttpStatus httpStatus, final int statusCode, final String responseMessage) {
        return res(httpStatus, statusCode, responseMessage, null);
    }

    public static<T> DefaultRes<T> res(final HttpStatus httpStatus, final int statusCode, final String responseMessage, final T t) {
        return DefaultRes.<T>builder()
                .httpStatus(httpStatus)
                .statusCode(statusCode)
                .responseMessage(responseMessage)
                .result(t)
                .build();
    }
}

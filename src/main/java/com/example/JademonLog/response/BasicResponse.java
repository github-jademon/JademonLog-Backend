package com.example.JademonLog.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasicResponse {

    private Integer code;

    private HttpStatus httpStatus;

    private String message;

    private Object result;

    public BasicResponse error(String message) {
        return BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message(message)
                .result(null)
                .build();
    }

    public BasicResponse success(String message, Object result) {
        return BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message(message)
                .result(result)
                .build();
    }

}
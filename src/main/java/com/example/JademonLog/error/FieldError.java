package com.example.JademonLog.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class FieldError {

    private String field;

    private String value;

    private String message;

    public static List<FieldError> of(final BindingResult bindingResult) {
        final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
        return fieldErrors.stream()
                .map(error -> new FieldError(
                        error.getField(),
                        error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());
    }

}
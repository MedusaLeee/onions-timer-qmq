package com.onions.timer.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ParamsInvalidException extends RuntimeException {
    private int code;
    private String message;
}

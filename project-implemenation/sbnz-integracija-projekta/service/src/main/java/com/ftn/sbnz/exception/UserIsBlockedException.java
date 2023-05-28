package com.ftn.sbnz.exception;

public class UserIsBlockedException extends RuntimeException{
    public UserIsBlockedException(String message) {
        super(message);
    }

    public UserIsBlockedException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.itheima.exception;

public class ClassHasStudentsException extends RuntimeException {
    public ClassHasStudentsException(String message) {
        super(message);
    }
}
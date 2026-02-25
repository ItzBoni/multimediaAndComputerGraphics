package com.javafxdemo.first_midterm.utils;

public class InvalidImageDimensionsException extends RuntimeException {

    // 1. Basic constructor
    public InvalidImageDimensionsException(String message) {
        super(message); // Passes the error message to the parent Exception class
    }

    // 2. Constructor for when you want to wrap another error
    public InvalidImageDimensionsException(String message, Throwable cause) {
        super(message, cause);
    }
}
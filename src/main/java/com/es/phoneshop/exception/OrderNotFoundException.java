package com.es.phoneshop.exception;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(String errorMessage) { super(errorMessage); }
}

package com.es.phoneshop.exception;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String errorMessage) { super(errorMessage); }
}

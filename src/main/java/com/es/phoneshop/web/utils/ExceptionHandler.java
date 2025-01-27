package com.es.phoneshop.web.utils;

import com.es.phoneshop.exception.OutOfStockException;

import java.text.ParseException;
import java.util.Map;

public class ExceptionHandler {
    public void handleError(Map<Long, String> errors, Long productId, Exception e) {
        if (e.getClass().equals(ParseException.class)) {
            errors.put(productId, "Not a number");
        } else if (e.getClass().equals(IllegalArgumentException.class)) {
            errors.put(productId, "Can't be negative");
        } else {
            errors.put(productId, "Out of stock" + ((OutOfStockException) e).getStockAvailable());
        }
    }
}


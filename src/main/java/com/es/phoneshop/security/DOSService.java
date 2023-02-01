package com.es.phoneshop.security;

public interface DOSService {
    boolean isAllowed(String ip);
    void cancel();
}

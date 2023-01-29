package com.es.phoneshop.security.impl;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.security.DOSService;
import com.es.phoneshop.service.impl.DefaultCartService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DOSServiceImpl implements DOSService {

    private static final long THRESHOLD = 30;

    private Map<String, Long> countMap = new ConcurrentHashMap();

    private static class SingeltonHelper {
        private static final DOSServiceImpl INSTANCE = new DOSServiceImpl();
    }

    public static synchronized DOSServiceImpl getInstance() {
        return DOSServiceImpl.SingeltonHelper.INSTANCE;
    }


    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        if(count == null){
            count = 1L;
        } else{
            if(count > THRESHOLD){
                return false;
            }
            count++;
        }
        countMap.put(ip, count);
        return true;
    }
}

package com.es.phoneshop.security.impl;

import com.es.phoneshop.security.DOSService;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class DOSServiceImpl implements DOSService {

    private static final long THRESHOLD = 30;
    private static final long TIMER_FOR_UNLOCK = 5000;
    private final Timer timer;

    private Map<String, Long> countMap = new ConcurrentHashMap<>();

    private DOSServiceImpl() {
        timer = new Timer();
        timer.scheduleAtFixedRate(getTask(), 1000, TIMER_FOR_UNLOCK);
    }

    private static class SingeltonHelper {
        private static final DOSServiceImpl INSTANCE = new DOSServiceImpl();
    }

    public static synchronized DOSServiceImpl getInstance() {
        return DOSServiceImpl.SingeltonHelper.INSTANCE;
    }

    public TimerTask getTask() {
        return new TimerTask() {
            @Override
            public void run() {
                countMap.clear();
            }
        };
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        if (count == null) {
            count = 1L;
        } else {
            if (count > THRESHOLD) {
                return false;
            }
            count++;
        }
        countMap.put(ip, count);
        return true;
    }

    @Override
    public void cancel() {
        if (timer != null) {
            timer.cancel();
        }
    }
}

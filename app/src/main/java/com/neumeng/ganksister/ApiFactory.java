package com.neumeng.ganksister;

import com.neumeng.ganksister.service.GankService;

/**
 * Created by koo on 2016/10/31.
 */

public class ApiFactory {
    protected static final Object monitor = new Object();
    static GankService sGankService = null;
    public static final int size = 20;

    public static GankService getGankServiceSingleton(){
        synchronized (monitor){
            if (sGankService==null){
                sGankService = new ApiClient().getGankService();
            }
            return sGankService;
        }
    }
}

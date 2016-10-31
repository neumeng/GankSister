package com.neumeng.ganksister;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neumeng.ganksister.service.GankService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by koo on 2016/10/31.
 */

public class ApiClient {
    final GankService gankService;


    ApiClient(){
        OkHttpClient client = new OkHttpClient();
        Gson gson = new GsonBuilder().serializeNulls().create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        gankService = retrofit.create(GankService.class);
    }
    public GankService getGankService(){
        return gankService;
    }
}

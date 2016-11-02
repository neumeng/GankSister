package com.neumeng.ganksister.service;

import com.neumeng.ganksister.ApiFactory;
import com.neumeng.ganksister.bean.RecordResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by koo on 2016/10/31.
 */

public interface GankService {
    @GET("data/all/40/1")
    Observable<RecordResult> getRecords();

    @GET("data/{type}/" + ApiFactory.size + "/{page}")
    Observable<RecordResult> getRecords(@Path("type") String type, @Path("page") int page);
}

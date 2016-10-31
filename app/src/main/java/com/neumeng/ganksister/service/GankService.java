package com.neumeng.ganksister.service;

import com.neumeng.ganksister.bean.RecordResult;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by koo on 2016/10/31.
 */

public interface GankService {
    @GET("data/all/40/1")
    Observable<RecordResult> getRecords();
}

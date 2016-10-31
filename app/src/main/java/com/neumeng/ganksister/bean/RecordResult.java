package com.neumeng.ganksister.bean;

import com.neumeng.ganksister.bean.base.BaseResult;

import java.util.List;

/**
 * Created by koo on 2016/10/31.
 */

public class RecordResult extends BaseResult{
    public List<RecordData> results;

    @Override
    public String toString() {
        return "RecordResult{" +
                "results=" + results +
                '}';
    }
}

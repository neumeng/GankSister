package com.neumeng.ganksister.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.neumeng.ganksister.ApiFactory;
import com.neumeng.ganksister.R;
import com.neumeng.ganksister.adapter.RecordsAdapter;
import com.neumeng.ganksister.bean.RecordData;
import com.neumeng.ganksister.bean.RecordResult;
import com.neumeng.ganksister.service.GankService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 程序主界面
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private GankService gankService = ApiFactory.getGankServiceSingleton();


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecordsRv;

    RecordsAdapter mAdapter;
    List<RecordData> mRecordDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initActionBar();
        initRecyclerView();
        loadData();
    }

    private void loadData() {
        Log.e(TAG, "start");
        gankService
                .getRecords()
                .subscribeOn(Schedulers.io())
                .map(recordResult -> recordResult.results)
                .map(recordDatas -> {
                        List<RecordData> datas= new ArrayList<>();
                            for (RecordData recordData:recordDatas
                                 ) {
                                if (!recordData.type.equals("福利")){
                                    datas.add(recordData);
                                }

                            }
                            return datas;
                        }
//                    recordDatas.stream().filter(recordData -> recordData.type.equals("福利")).into(new List<RecordData>());
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recordDatas -> {
                    mRecordDatas.clear();
                    mRecordDatas.addAll(recordDatas);
                    mAdapter.notifyDataSetChanged();
                });
        Log.e(TAG, "end");
    }

    private void initRecyclerView() {
        mRecordDatas = new ArrayList<>();
        mAdapter = new RecordsAdapter(this, mRecordDatas);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecordsRv.setLayoutManager(manager);
        mRecordsRv.setAdapter(mAdapter);
    }

    private void initActionBar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

//    private Observable<List<RecordData>> getRecordDatas(RecordResult){
//
//    }
}

package com.neumeng.ganksister.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.neumeng.ganksister.ApiFactory;
import com.neumeng.ganksister.R;
import com.neumeng.ganksister.adapter.MeizhiAdapter;
import com.neumeng.ganksister.bean.RecordData;
import com.neumeng.ganksister.service.GankService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MeizhiActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    ActionBar mActionBar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecordsRv;
    @BindView(R.id.swipe)
    SwipeRefreshLayout mSwipe;
    private GankService gankService = ApiFactory.getGankServiceSingleton();
    private int mCurrentPage = 1;//当前页
    MeizhiAdapter mAdapter;
    List<RecordData> mRecordDatas;

    private boolean mIsLoadingMore = false;//是否是在加载更多
    private boolean mIsFirstTimeTouchBottom=true;

    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, MeizhiActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meizhi);
        ButterKnife.bind(this);
        initActionBar();
        initRecyclerView();
        loadData(true);
    }
    private void initActionBar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setTitle("妹子们");
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

    }
    private void initRecyclerView() {
        mRecordDatas = new ArrayList<>();
        mAdapter = new MeizhiAdapter(this, mRecordDatas);
        mAdapter.setOnClickListener((v, position) ->
                startActivity(WebActivity.newIntent(this, mRecordDatas.get(position).url))
        );
        RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecordsRv.setLayoutManager(manager);
        mRecordsRv.setAdapter(mAdapter);
        mRecordsRv.addOnScrollListener(getOnBottomListener((StaggeredGridLayoutManager) manager));
        mSwipe.setOnRefreshListener(() -> loadData(true));
        mSwipe.setColorSchemeResources(R.color.primary, R.color.accent);
    }
  /*  RecyclerView.OnScrollListener getOnBottomListener(final StaggeredGridLayoutManager manager) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int lastVisibleItem[] = manager.findLastVisibleItemPositions(new int[2]);
                int totalItemCount = manager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem[1] >= totalItemCount - 6 && dy > 0) {
                    if (mIsLoadingMore) {
                        //正在加载，忽略
                    } else {
                        loadData(false);
                        mIsLoadingMore = true;
                    }
                }
            }
        };
    }*/
    RecyclerView.OnScrollListener getOnBottomListener(StaggeredGridLayoutManager layoutManager) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                boolean isBottom =
                        layoutManager.findLastCompletelyVisibleItemPositions(new int[2])[1] >=
                                mAdapter.getItemCount() - 6;//是否到底部
                if (!mSwipe.isRefreshing() && isBottom) {//如果不是刷新而且到达 底部，需要 刷新
                    if (!mIsFirstTimeTouchBottom) {//是否第1次到底，默认是true
                        mSwipe.setRefreshing(true);
                        mCurrentPage += 1;
                        loadData(false);
                    } else {
                        mIsFirstTimeTouchBottom = false;
                    }
                }
            }
        };
    }
    public void setRefresh(boolean requestDataRefresh) {
        if (mSwipe == null) {
            return;
        }
        if (!requestDataRefresh) {
            mIsLoadingMore = false;
            // 防止刷新消失太快，让子弹飞一会儿.
            mSwipe.postDelayed(() -> {
                if (mSwipe != null) {
                    mSwipe.setRefreshing(false);
                }
            }, 1000);
        } else {
            mSwipe.setRefreshing(true);
        }
    }
    private void loadData(boolean clean) {
        mSwipe.setRefreshing(true);
        if (clean) {
            mRecordDatas.clear();
            mCurrentPage = 1;
        }
        gankService
                .getRecords("福利", mCurrentPage)
                .subscribeOn(Schedulers.io())
                .map(recordResult -> recordResult.results)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recordDatas -> {
                    mRecordDatas.addAll(recordDatas);
                    mAdapter.notifyDataSetChanged();
                    mCurrentPage++;
                    setRefresh(false);
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

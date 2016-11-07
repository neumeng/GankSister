package com.neumeng.ganksister.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.neumeng.ganksister.ApiFactory;
import com.neumeng.ganksister.R;
import com.neumeng.ganksister.adapter.MainFragmentPagerAdapter;
import com.neumeng.ganksister.adapter.RecordsAdapter;
import com.neumeng.ganksister.bean.RecordData;
import com.neumeng.ganksister.bean.RecordResult;
import com.neumeng.ganksister.fragment.RecordsFragment;
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

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.pager)
    ViewPager mPager;
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    MainFragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initActionBar();
        initPagers();
        initTabs();
        initFab();
    }

    private void initFab() {
        mFab.setOnClickListener(view -> startActivity(MeizhiActivity.newIntent(MainActivity.this)));
    }

    private void initPagers() {
        RecordsFragment androidFragment = RecordsFragment.newInstance("Android");
        RecordsFragment iosFragment = RecordsFragment.newInstance("iOS");
        RecordsFragment appFragment = RecordsFragment.newInstance("App");

        mAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(androidFragment, "Android");
        mAdapter.addFragment(iosFragment, "iOS");
        mAdapter.addFragment(appFragment, "App");
        mPager.setOffscreenPageLimit(2);//一共3个fragment，保持fragment不销毁
        mPager.setAdapter(mAdapter);
    }

    private void initTabs() {
        mTabs.setupWithViewPager(mPager);
    }


    private void initActionBar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }


}

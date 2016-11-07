package com.neumeng.ganksister.fragment;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neumeng.ganksister.ApiFactory;
import com.neumeng.ganksister.R;
import com.neumeng.ganksister.activity.WebActivity;
import com.neumeng.ganksister.adapter.RecordsAdapter;
import com.neumeng.ganksister.bean.RecordData;
import com.neumeng.ganksister.service.GankService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordsFragment extends Fragment {
    private static final String TAG = "RecordsFragment";
    private static final String ARG_TYPE = "type";
    private GankService gankService = ApiFactory.getGankServiceSingleton();
    private String mType;//类型
    private int mCurrentPage = 1;//当前页
    @BindView(R.id.recycler_view)
    RecyclerView mRecordsRv;
    @BindView(R.id.swipe)
    SwipeRefreshLayout mSwipe;
    RecordsAdapter mAdapter;
    List<RecordData> mRecordDatas;

    private boolean mIsLoadingMore = false;//是否是在加载更多


    public RecordsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param type 记录类型.
     * @return A new instance of fragment RecordsFragment.
     */
    public static RecordsFragment newInstance(String type) {
        RecordsFragment fragment = new RecordsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_records, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();
        loadData(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initRecyclerView() {
        mRecordDatas = new ArrayList<>();
        mAdapter = new RecordsAdapter(getActivity(), mRecordDatas);
        mAdapter.setOnClickListener((v, position) ->
                startActivity(WebActivity.newIntent(getActivity(), mRecordDatas.get(position).url))
        );
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecordsRv.setLayoutManager(manager);
        mRecordsRv.setAdapter(mAdapter);
        mRecordsRv.addOnScrollListener(getOnBottomListener((LinearLayoutManager) manager));

        mRecordsRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });

        mRecordsRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        mSwipe.setOnRefreshListener(() -> loadData(true));
        mSwipe.setColorSchemeResources(R.color.primary, R.color.accent);
    }

    private void loadData(boolean clean) {
        mSwipe.setRefreshing(true);
        if (clean) {
            mRecordDatas.clear();
            mCurrentPage = 1;
        }
        gankService
                .getRecords(mType, mCurrentPage)
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

    RecyclerView.OnScrollListener getOnBottomListener(final LinearLayoutManager manager) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = manager.findLastVisibleItemPosition();
                int totalItemCount = manager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    if (mIsLoadingMore) {
                        //正在加载，忽略
                    } else {
                        loadData(false);
                        mIsLoadingMore = true;
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
}

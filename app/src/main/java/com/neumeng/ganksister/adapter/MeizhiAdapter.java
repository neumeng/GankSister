package com.neumeng.ganksister.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.neumeng.ganksister.R;
import com.neumeng.ganksister.bean.RecordData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KOO on 2016-11-07.
 */

public class MeizhiAdapter extends RecyclerView.Adapter<MeizhiAdapter.MyViewHolder> {
    private List<RecordData> mRecordDatas;
    private Context mContext;
    OnClickListener mOnClickListener;

    public MeizhiAdapter(Context context, List<RecordData> recordDatas) {
        mContext = context;
        mRecordDatas = recordDatas;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public MeizhiAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_meizhi, parent, false);
        return new MeizhiAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MeizhiAdapter.MyViewHolder holder, int position) {
        RecordData recordData = mRecordDatas.get(position);
        Glide.with(mContext).load(recordData.url).into(holder.meizhi);
    }

    @Override
    public int getItemCount() {
        return mRecordDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.meizhi)
        ImageView meizhi;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (mOnClickListener != null) {
                    mOnClickListener.click(v, getAdapterPosition());
                }
            });
        }
    }

    public interface OnClickListener {
        void click(View view, int position);
    }
}

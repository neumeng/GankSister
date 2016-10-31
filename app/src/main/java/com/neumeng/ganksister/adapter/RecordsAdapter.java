package com.neumeng.ganksister.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.neumeng.ganksister.R;
import com.neumeng.ganksister.bean.RecordData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by koo on 2016/10/31.
 */

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.MyViewHolder> {
    private List<RecordData> mRecordDatas;
    private Context mContext;

    public RecordsAdapter(Context context,List<RecordData> recordDatas) {
        mContext = context;
        mRecordDatas = recordDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_record_data, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.desc.setText(mRecordDatas.get(position).desc);
    }

    @Override
    public int getItemCount() {
        return mRecordDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.desc)
        TextView desc;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


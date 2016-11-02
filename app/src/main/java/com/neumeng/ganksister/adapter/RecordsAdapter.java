package com.neumeng.ganksister.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    OnClickListener mOnClickListener;

    public RecordsAdapter(Context context, List<RecordData> recordDatas) {
        mContext = context;
        mRecordDatas = recordDatas;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_record_data, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RecordData recordData = mRecordDatas.get(position);
        holder.desc.setText(recordData.desc.length() > 40 ? recordData.desc.substring(0, 40) + "..." : recordData.desc);
//        List<String> urls = recordData.images;
        if (recordData.images != null && recordData.images.size() > 0) {
            String url = recordData.images.get(0);
            Glide.with(mContext).load(url + "?imageView2/0/w/72").into(holder.pic);
        } else {
            holder.pic.setImageResource(R.mipmap.ic_launcher);
        }
    }

    @Override
    public int getItemCount() {
        return mRecordDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.pic)
        ImageView pic;
        @BindView(R.id.desc)
        TextView desc;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (mOnClickListener != null) {
                    mOnClickListener.click(v,getAdapterPosition());
                }
            });
        }
    }

    public interface OnClickListener {
        void click(View view,int position);
    }
}


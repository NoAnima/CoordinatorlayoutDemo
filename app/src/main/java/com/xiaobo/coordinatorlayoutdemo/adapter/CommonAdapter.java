package com.xiaobo.coordinatorlayoutdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by maxb on 2017/4/20.
 */

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected  Context mContext;
    protected  int mLayoutId;
    protected  List<T> mDatas;
    protected LayoutInflater mInflater;
    protected  ViewGroup mRv;
    private OnItemClickListener mOnItemClickListener;

    public CommonAdapter setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
        return  this;
    }

    public CommonAdapter (Context context, List<T> datas,int layoutId){
        this.mContext=context;
        this.mInflater=LayoutInflater.from(context);
        this.mLayoutId=layoutId;
        this.mDatas=datas;
    }

    public OnItemClickListener getmOnItemClickListener(){
        return  this.mOnItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

package com.xiaobo.coordinatorlayoutdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaobo.coordinatorlayoutdemo.Meizis;
import com.xiaobo.coordinatorlayoutdemo.R;

import java.util.List;

/**
 * Created by xiaobo on 2017/4/11.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private  Context mContext;
    private List<Meizis> mlist;
    public RecyclerAdapter(Context context, List<Meizis> list){
        mContext=context;
        mlist=list;
    }

    public void setData(List<Meizis> data){
       if (data!=null){
           mlist=data;
       }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==-1){
            MyViewHolder holder=new MyViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.line_recyler_item,parent,false));
        }
        MyViewHolder holder=new MyViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.line_recyler_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mlist==null||mlist.size()<0)
            return;
        ((MyViewHolder)holder).textView.setText("图"+ position);
        Picasso.with(mContext).load(mlist.get(position).getUrl()).into(((MyViewHolder) holder).imageView);
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.line_item_tv);
            imageView= (ImageView) itemView.findViewById(R.id.line_item_iv);
        }
    }

    public void addItem(Meizis meizi,int position){
        mlist.add(position,meizi);
        notifyItemInserted(position);

    }
    public void removeItem(int position){
        Meizis meizi=mlist.get(position);
        mlist.remove(position);
        notifyItemRemoved(position);

    }
}

package com.xiaobo.coordinatorlayoutdemo.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by maxb on 2017/4/20.
 */

public interface OnItemClickListener <T>{
    void onItemClick(ViewGroup var1, View var2,T var3,int var4);
    boolean onItemLongClick(ViewGroup var1,View var2,T var3,int var4);
}

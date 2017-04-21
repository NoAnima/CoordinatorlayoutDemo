package com.xiaobo.coordinatorlayoutdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.ImageView;

import com.mcxtzhang.commonadapter.rv.CommonAdapter;
import com.mcxtzhang.commonadapter.rv.ViewHolder;
import com.squareup.picasso.Picasso;
import com.xiaobo.coordinatorlayoutdemo.CardCallback;
import com.xiaobo.coordinatorlayoutdemo.CardConfig;
import com.xiaobo.coordinatorlayoutdemo.OverCardLayoutMangeer;
import com.xiaobo.coordinatorlayoutdemo.R;
import com.xiaobo.coordinatorlayoutdemo.bean.SwipeCardBean;

import java.util.List;


/**
 * Created by maxb on 2017/4/19.
 */

public class SwipeCardActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private CommonAdapter<SwipeCardBean> mAdapter;
    private List<SwipeCardBean> mDatas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_card);
        inintView();
        initData();
    }

    private void inintView(){
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.swipe_card_coordinator);
        recyclerView= (RecyclerView) findViewById(R.id.swipe_card_recycler);
        recyclerView.setLayoutManager(new OverCardLayoutMangeer());
        recyclerView.setAdapter(mAdapter=new CommonAdapter<SwipeCardBean>(this,mDatas=SwipeCardBean.initDatas(),R.layout.over_card_item) {
            @Override
            public void convert(ViewHolder viewHolder, SwipeCardBean swipeCardBean) {
                viewHolder.setText(R.id.tvName,swipeCardBean.getName());
                viewHolder.setText(R.id.tvPrecent,swipeCardBean.getPostition()+"/"+mDatas.size());
                Picasso.with(SwipeCardActivity.this).load(swipeCardBean.getUrl()).into((ImageView) viewHolder.getView(R.id.iv));
            }
        });
        CardConfig.initConfig(this);
        ItemTouchHelper.Callback callback=new CardCallback(recyclerView,mAdapter,mDatas);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }
    private void initData(){

    }

}

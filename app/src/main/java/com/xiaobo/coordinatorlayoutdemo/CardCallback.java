package com.xiaobo.coordinatorlayoutdemo;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.List;

import static com.xiaobo.coordinatorlayoutdemo.CardConfig.MAX_SHOW_COUNT;
import static com.xiaobo.coordinatorlayoutdemo.CardConfig.SCALE_GAP;
import static com.xiaobo.coordinatorlayoutdemo.CardConfig.TRANS_Y_GAP;

/**
 * Created by maxb on 2017/4/22.
 */

public class CardCallback extends ItemTouchHelper.SimpleCallback{


    private List mDatas;
    private RecyclerView mRv;
    private RecyclerView.Adapter mAdapter;


    public CardCallback(int dragDirs, int swipeDirs,
                        RecyclerView rv, RecyclerView.Adapter adapter, List datas) {
        super(dragDirs, swipeDirs);
        mRv=rv;
        mAdapter=adapter;
        mDatas=datas;


    }
    public CardCallback(RecyclerView rv, RecyclerView.Adapter adapter, List datas){
        this(0,ItemTouchHelper.DOWN|ItemTouchHelper.UP|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT,
                rv,adapter,datas);
    }
    //水平方向是否可以被回收掉的阈值
    public float getTheshold(RecyclerView.ViewHolder viewHolder){
        // 考虑 探探垂直上下方向滑动，不删除卡片，这里参照源码写死0.5f
        return  mRv.getWidth()*/*getSwipeThreshold(viewHolder)*/05f;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        Object o= mDatas.remove(viewHolder.getLayoutPosition());
        mDatas.add(0,o);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        double swipeValue=Math.sqrt(dX*dX+dY*dY);
        double fraction =swipeValue/getTheshold(viewHolder);
        if (fraction>1){
            fraction=1;
        }
        int  childCount=recyclerView.getChildCount();
        for (int i=0;i<childCount;i++){
            View childView=recyclerView.getChildAt(i);
            int level =childCount-i-1;
            if (level>0){
                childView.setScaleX((float) (1- SCALE_GAP*level+fraction* SCALE_GAP));
                if (level < MAX_SHOW_COUNT - 1) {
                    childView.setScaleY((float) (1 - SCALE_GAP * level + fraction * SCALE_GAP));
                    childView.setTranslationY((float) (TRANS_Y_GAP * level - fraction * TRANS_Y_GAP));
                } else {
                    //child.setTranslationY((float) (mTranslationYGap * (level - 1) - fraction * mTranslationYGap));
                }

            }

        }


    }
}

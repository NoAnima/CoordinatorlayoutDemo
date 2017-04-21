package com.xiaobo.coordinatorlayoutdemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by maxb on 2017/4/19.
 */

public class OverCardLayoutMangeer extends RecyclerView.LayoutManager{

    private static final String TAG = "swipecard";
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        detachAndScrapAttachedViews(recycler);
        int itemCount=getItemCount();
        if (itemCount<1){
            return;
        }
        int bottomPosition;
        if (itemCount<CardConfig.MAX_SHOW_COUNT){
            bottomPosition=0;
        }else {
            bottomPosition=itemCount-CardConfig.MAX_SHOW_COUNT;
        }
        for (int position=bottomPosition;position<itemCount;position++){
            View view=recycler.getViewForPosition(position);
            addView(view);
            measureChild(view,0,0);
            int withSpace=getWidth()-getDecoratedMeasuredWidth(view);
            int heightSpace=getHeight()-getDecoratedMeasuredWidth(view);

            layoutDecoratedWithMargins(view,withSpace/2,heightSpace/2,withSpace/2+getDecoratedMeasuredWidth(view),
                    heightSpace/2+getDecoratedMeasuredHeight(view));

            int level=itemCount-position-1;

            if (level>0){
                view.setScaleX(1-CardConfig.SCALE_GAP*level);
                if (level<CardConfig.MAX_SHOW_COUNT-1){
                    view.setTranslationY(CardConfig.TRANS_Y_GAP*level);
                    view.setScaleY(1-CardConfig.SCALE_GAP*level);
                }else {
                    view.setTranslationY(CardConfig.TRANS_Y_GAP*(level-1));
                    view.setScaleY(1-CardConfig.SCALE_GAP*(level-1));
                }
            }
        }
    }
}

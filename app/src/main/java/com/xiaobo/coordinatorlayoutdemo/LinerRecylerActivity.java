package com.xiaobo.coordinatorlayoutdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaobo.coordinatorlayoutdemo.adapter.RecyclerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

/**
 * Created by xiaobo on 2017/4/12.
 */

public class LinerRecylerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayoutManager linearLayoutManager;
    private ItemTouchHelper itemTouchHelper;

    private List<Meizis> meizis;
    private RecyclerAdapter adapter;

    private int screenwidth;
    private int lastVisibleItem;

    private int page=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_line);
        setSupportActionBar(toolbar);

        initView();
        setListener();
        new GetData().execute("http://gank.io/api/data/福利/10/1");

        //获取屏幕宽度
        WindowManager wm= (WindowManager) LinerRecylerActivity.this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        screenwidth=outMetrics.widthPixels;
    }

    private void initView(){
        recyclerView= (RecyclerView) findViewById(R.id.recycler_line);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.coordinator_line);

        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_line);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimaryDark,R.color.colorAccent);
        // 稍后查一下  setProgressViewOffset 方法的参数是什么意思
        swipeRefreshLayout.setProgressViewOffset(false,0,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

    }


    private void setListener(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                new GetData().execute("http://gank.io/api/data/福利/10/1");
            }
        });

        itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
               int dragFlags=0,swipeFlags=0;
                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager){
                    dragFlags=ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;

                }else if (recyclerView.getLayoutManager()instanceof LinearLayoutManager){
                    //设置侧滑方向为从左到右和从右到左都可以
                    swipeFlags=ItemTouchHelper.START|ItemTouchHelper.END;
                }
                return makeMovementFlags(dragFlags,swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from=viewHolder.getAdapterPosition();
                int to =target.getAdapterPosition();
                Collections.swap(meizis,from,to);
                adapter.notifyItemMoved(from,to);
                return true;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.removeItem(viewHolder.getAdapterPosition());

                SnackbarUtil.ShortSnackbar(coordinatorLayout,"你删除了第"+viewHolder.getAdapterPosition()+"个item",SnackbarUtil.Warning).setAction("撤销", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        adapter.addItem(adapter.,viewHolder.getAdapterPosition());
                        SnackbarUtil.ShortSnackbar(coordinatorLayout,"撤销了删除第"+viewHolder.getAdapterPosition()+"个item",SnackbarUtil.Confirm).show();
                    }
                }).setActionTextColor(Color.WHITE).show();
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                if (actionState==ItemTouchHelper.ACTION_STATE_DRAG){
                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
               viewHolder.itemView.setAlpha(1-Math.abs(dX)/screenwidth);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == recyclerView.SCROLL_STATE_IDLE && lastVisibleItem+2>=linearLayoutManager.getItemCount()){
                    new GetData().execute("http://gank.io/api/data/福利/10/"+(++page));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem=linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }
    private class GetData extends AsyncTask<String,Integer,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {
            return MyOkHttp.get(params[0]);
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!TextUtils.isEmpty(s)){
                JSONObject jsonObject;
                Gson gson=new Gson();
                String jsonData=null;


                try {
                    jsonObject=new JSONObject(s);
                    jsonData=jsonObject.getString("results");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (meizis==null||meizis.size()==0){
                    meizis=gson.fromJson(jsonData,new TypeToken<List<Meizis>>(){}.getType());
                    Meizis pages=new Meizis();
                    pages.setPage(page);
                    meizis.add(pages);
                }else {
                    List<Meizis> more=gson.fromJson(jsonData,new TypeToken<List<Meizis>>(){}.getType());
                    meizis.addAll(more);

                    Meizis pages=new Meizis();
                    pages.setPage(page);
                    meizis.add(pages);
                }

                if (adapter==null){
                    recyclerView.setAdapter(adapter=new RecyclerAdapter(LinerRecylerActivity.this,meizis));
                    itemTouchHelper.attachToRecyclerView(recyclerView);
                }else {
                    adapter.notifyDataSetChanged();
                }
                swipeRefreshLayout.setRefreshing(false);

            }
        }
    }
}

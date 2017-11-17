package com.dc.moments.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dc.moments.R;
import com.dc.moments.base.Global;
import com.dc.moments.bean.Moment;
import com.dc.moments.bean.UserInfo;
import com.dc.moments.commom.Constant;
import com.dc.moments.eventbus.MomentsEvent;
import com.dc.moments.ui.adapter.MomentsAdapter;
import com.dc.moments.util.ImagerLoaderUtil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private String momentsJson, userInfoJson;
    private TextView tv_name;
    private ImageView iv_personal,iv_background;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipe_container;

    int lastVisibleItem = 0;
    int index = 1;
    int temp = 0;
    boolean isFirstLoda = true;
    List<Moment> momentList = new ArrayList<>();
    private MomentsAdapter momentsAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_personal = (ImageView) findViewById(R.id.iv_personal);
        iv_background = (ImageView) findViewById(R.id.iv_background);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipe_container = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        getJson(Constant.TWEETS_URL, Constant.REQUEST_TWEETS);
//        getMomentsJson();
//        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //注册EventBus监听
        EventBus.getDefault().register(this);
        //设置SwipeRefreshLayout
        initSwipeRefresh();
    }

    private void initSwipeRefresh() {
//        swipe_container.setColorSchemeColors();
        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lastVisibleItem = 0;
                isFirstLoda = true;
                momentList.clear();
                index = 1;
                getData(index);
            }
        });

        //监听recyclerView的上滑动的位置来进行积蓄的加载更多的数据
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            //滚动中调用
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取总的适配器的数量
                int totalCount = momentsAdapter.getItemCount();
                //这个就是判断当前滑动停止了，并且获取当前屏幕最后一个可见的条目是第几个，当前屏幕数据已经显示完毕的时候就去加载数据
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 ==
                        momentsAdapter.getItemCount()) {
                    swipe_container.setRefreshing(true);//刷新完毕!
                    //请求数据
                    index++;
                    getData(index);
                }
            }

            //滚动停止后调用
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取最后一个可见的条目的位置,如果是线性加载更多就换成这个
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    //从缓存里面拿数据
    private void getData(int index) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁EventBus监听
        EventBus.getDefault().unregister(this);
    }

    /**
     * 从后台获取朋友圈的Json数据
     */
    public void getJson(String url, final int REQUEST_CODE) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "网络错误...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                EventBus.getDefault().post(new MomentsEvent(data, REQUEST_CODE));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMoments(MomentsEvent event) {
        int REQUEST_CODE = event.getRequestCode();
        if (REQUEST_CODE == 1) {
            momentsJson = event.getData();
            getJson(Constant.USER_INFO_URL, Constant.REQUEST_USER_INFO);
        } else if (REQUEST_CODE == 2) {
            userInfoJson = event.getData();
            initUserInfo(userInfoJson);
            initRecyclerView(momentsJson);
        }
    }

    //填充个人信息avatar、nick、name
    private void initUserInfo(String userInfoJson) {
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(userInfoJson, UserInfo.class);
        tv_name.setText(userInfo.getUsername());

        //头像显示
        ImagerLoaderUtil.getInstance(this).displayMyImage(userInfo.getAvatar(),
                iv_personal, new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        iv_personal.setImageBitmap(loadedImage);
                    }
                });
        //背景图片显示
        ImagerLoaderUtil.getInstance(this).displayMyImage(userInfo.getProfile_image(),
                iv_background, new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        iv_background.setImageBitmap(loadedImage);
                    }
                });


    }

    private void initRecyclerView(String momentsJson) {
        // 请求到后台Json数据后列表显示
        Gson gson = new Gson();

        /**
         * 这里由于从后台拿到的Json数据不不符合Gson解析规则，导致Gson解析时出错；
         * 解决办法是：1、后台修改Json数据
         *           2、前端对拿到的字符串进行规范处理
         * 这里采取了方法2不过没有在代码里面处理字符串，在sublime里面处理后直接放到assets下拿来用了
         */
        String json = Global.readAssets("moments.json");
        Moment moment = gson.fromJson(json, Moment.class);

        // 显示朋友圈列表
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        momentsAdapter = new MomentsAdapter(this, moment.getMoments());
        recyclerView.setAdapter(momentsAdapter);
    }
}

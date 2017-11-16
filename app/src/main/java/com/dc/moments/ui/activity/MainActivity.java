package com.dc.moments.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dc.moments.R;
import com.dc.moments.base.Global;
import com.dc.moments.bean.Moment;
import com.dc.moments.commom.Constant;
import com.dc.moments.eventbus.MomentsEvent;
import com.dc.moments.ui.adapter.MomentsAdapter;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private String momentsJson,userInfoJson;
    private LinearLayout llTitleBar,ll_personal;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private TextView tvLike;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        llTitleBar = (LinearLayout) findViewById(R.id.ll_title_bar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        getJson(Constant.TWEETS_URL,Constant.REQUEST_TWEETS);
//        getMomentsJson();
//        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //注册EventBus监听
        EventBus.getDefault().register(this);
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
    public void getJson(String url, final int REQUEST_CODE){
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
                EventBus.getDefault().post(new MomentsEvent(data,REQUEST_CODE));
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMoments(MomentsEvent event){
        int REQUEST_CODE = event.getRequestCode();
        if (REQUEST_CODE == 1) {
            momentsJson = event.getData();
            getJson(Constant.USER_INFO_URL,Constant.REQUEST_USER_INFO);
        } else if (REQUEST_CODE == 2) {
            userInfoJson = event.getData();
            initUserInfo(userInfoJson);
            initRecyclerView(momentsJson);
        }
    }

    private void initUserInfo(String userInfoJson) {

    }

    private void initRecyclerView(String momentsJson) {
        // 请求到后台Json数据后列表显示
        Gson gson = new Gson();

        /**
         * 这里由于从后台拿到的Json数据不规范，开始出包含空字符串，导致Gson解析时出错；
         * 解决办法是：1、后台修改Json数据
         *           2、前端对拿到的字符串进行规范处理
         * 这里采取了方法2不过没有在代码里面处理字符串，在sublime里面处理后直接放到assets下拿来用了
         */
        String json = Global.readAssets("moments.json");
        Moment moment = gson.fromJson(json, Moment.class);

        // 显示朋友圈列表
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MomentsAdapter(this, moment.getMoments()));
    }

    public void animateUp(int[] locations) {
        // 减去状态栏高度24dp
        int currentY = locations[1] - Global.dp2px(24);
        tvLike.setVisibility(View.VISIBLE);
        tvLike.setTranslationX(locations[0]);
        tvLike.setTranslationY(currentY);
        tvLike.setScaleY(1);
        tvLike.setScaleX(1);
        tvLike.setAlpha(1f);

        // 往上移动30dp
        int top = currentY - Global.dp2px(30);
        tvLike.animate().alpha(0).translationY(top)
                .setInterpolator(new DecelerateInterpolator())
                .scaleX(1.2f).scaleY(1.2f).setDuration(1000);
    }
}

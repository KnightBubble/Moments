package com.dc.moments.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
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
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    //保存文件的路径
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/moments.txt";
    private String momentsJson, userInfoJson;
    private TextView tv_name;
    private ImageView iv_personal, iv_background;
    private RecyclerView recyclerView;

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

        getJson(Constant.TWEETS_URL, Constant.REQUEST_TWEETS);
//        getMomentsJson();
//        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //注册EventBus监听
        EventBus.getDefault().register(this);
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
            saveData(momentsJson);
        }
    }

    private void initRecyclerView() {
        List<Moment.MomentBean> moments = readData();
        // 显示朋友圈列表
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        momentsAdapter = new MomentsAdapter(this, moments);
        recyclerView.setAdapter(momentsAdapter);
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

    //保存数据
    private void saveData(String momentsJson) {
        // 请求到后台Json数据后列表显示
        Gson gson = new Gson();
        List<Moment.MomentBean> moments = gson.fromJson(momentsJson, new
                TypeToken<ArrayList<Moment.MomentBean>>() {
        }.getType());

        //缓存数据
        ObjectOutputStream fos = null;
        try {
            File file = new File(path);
            //获取输出流
            //这里如果文件不存在会创建文件，这是写文件和读文件不同的地方
            fos = new ObjectOutputStream(new FileOutputStream(file));
            fos.writeObject(moments);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                    initRecyclerView();
                }
            } catch (IOException e) {
            }
        }
    }

    //读取数据
    private List<Moment.MomentBean> readData() {
        ObjectInputStream ois = null;
        Object moments = null;
        try {
            //获取输入流
            ois = new ObjectInputStream(new FileInputStream(new File(path)));
            //获取文件中的数据
            moments = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (List<Moment.MomentBean>) moments;
    }
}

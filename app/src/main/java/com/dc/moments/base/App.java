package com.dc.moments.base;

import android.app.Application;

import com.dc.moments.util.ImagerLoaderUtil;

/**
 * Created by chenzhiwei on 17/11/16.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化Global类
        Global.init(this);
        //初始化ImagerLoader
        ImagerLoaderUtil.getInstance(getApplicationContext()).initImageLoader();
    }
}

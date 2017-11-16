package com.dc.moments.util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by chenzhiwei on 17/11/17.
 */

public class OkHttpUtil {
    public void get(String url){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

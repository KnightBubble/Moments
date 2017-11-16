package com.dc.moments.eventbus;

/**
 * Created by chenzhiwei on 17/11/17.
 */

public class MomentsEvent {
    private String data;
    private int requestCode;

    public MomentsEvent(String data, int requestCode) {
        this.data = data;
        this.requestCode = requestCode;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public String getData() {
        return data;
    }
}

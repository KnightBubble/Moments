package com.dc.moments.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.dc.moments.ui.holder.ImageHolder;
import com.dc.moments.ui.holder.MyBaseHolder;

import java.util.List;

/**
 * Created by chenzhiwei on 17/11/16.
 */

public class ImageAdapter extends MyBaseAdapter<String> {
    public ImageAdapter(Context context, List<String> listData) {
        super(context, listData);
    }

    @Override
    public MyBaseHolder<String> createViewHolder(
            Context context, ViewGroup parent, int viewType) {
        return new ImageHolder(context, parent, this, viewType);
    }
}

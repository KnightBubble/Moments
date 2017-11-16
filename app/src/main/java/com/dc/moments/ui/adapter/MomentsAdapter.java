package com.dc.moments.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dc.moments.bean.Moment;
import com.dc.moments.ui.holder.MomentsHolder;
import com.dc.moments.ui.holder.MyBaseHolder;

import java.util.List;

/**
 * Created by chenzhiwei on 17/11/16.
 */

public class MomentsAdapter extends MyBaseAdapter<Moment.MomentBean> {


    public MomentsAdapter(Context context, List<Moment.MomentBean> listData) {
        super(context, listData);
    }

    @Override
    public MyBaseHolder<Moment.MomentBean> createViewHolder(
            Context context, ViewGroup parent, int viewType) {
        return new MomentsHolder(context, parent, this, viewType);
    }
}

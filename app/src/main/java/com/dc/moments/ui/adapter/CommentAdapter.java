package com.dc.moments.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dc.moments.bean.Moment;
import com.dc.moments.ui.holder.CommentHolder;
import com.dc.moments.ui.holder.MyBaseHolder;

import java.util.List;

/**
 * Created by chenzhiwei on 17/11/17.
 */

public class CommentAdapter extends MyBaseAdapter<Moment.MomentBean.CommentsEntity>{


    public CommentAdapter(Context context, List<Moment.MomentBean.CommentsEntity> listData) {
        super(context, listData);
    }

    @Override
    public MyBaseHolder<Moment.MomentBean.CommentsEntity> createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new CommentHolder(context,parent,this,viewType);
    }
}

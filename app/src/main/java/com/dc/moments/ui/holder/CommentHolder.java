package com.dc.moments.ui.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dc.moments.R;
import com.dc.moments.bean.Moment;
import com.dc.moments.ui.adapter.CommentAdapter;

/**
 * Created by chenzhiwei on 17/11/17.
 */

public class CommentHolder extends MyBaseHolder<Moment.MomentBean.CommentsEntity> {
    private TextView tv_senderName,tv_comment;
    public CommentHolder(Context context, ViewGroup parent, CommentAdapter commentAdapter, int
            viewType) {
        super(context,parent,commentAdapter,viewType, R.layout.item_comments);
    }

    // 查找item中的子控件
    @Override
    public void onFindViews(View itemView) {
        tv_senderName = (TextView) itemView.findViewById(R.id.tv_senderName);
        tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
    }

    // 刷新item子控件的显示
    @Override
    protected void onRefreshView(Moment.MomentBean.CommentsEntity bean, int position) {
        //显示评论
        tv_senderName.setText(bean.getSender().getUsername());
        tv_comment.setText(bean.getContent());
    }
}

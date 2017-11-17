package com.dc.moments.ui.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dc.moments.R;
import com.dc.moments.base.Global;
import com.dc.moments.bean.Moment;
import com.dc.moments.ui.activity.MainActivity;
import com.dc.moments.ui.adapter.CommentAdapter;
import com.dc.moments.ui.adapter.ImageAdapter;
import com.dc.moments.ui.adapter.MyBaseAdapter;
import com.dc.moments.util.ImagerLoaderUtil;
import com.dc.moments.util.LinkifyUtil;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhiwei on 17/11/16.
 */

public class MomentsHolder extends MyBaseHolder<Moment.MomentBean> {

    private ImageView ivAvatar;
    private TextView tvUsername;
    private TextView tvNick;
    private TextView tvContent;
    private RecyclerView rvImages,recycler_comments;
    private ImageAdapter imageAdapter;
    private CommentAdapter commentAdapter;
    private GridLayoutManager layoutManager;

    public MomentsHolder(Context context, ViewGroup parent,
                         MyBaseAdapter<Moment.MomentBean> adapter,
                         int itemType) {
        super(context, parent, adapter, itemType, R.layout.item_moment);
    }

    // 查找item布局中的子控件
    @Override
    public void onFindViews(View itemView) {
        ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
        tvNick = (TextView) itemView.findViewById(R.id.tv_nick);
        tvContent = (TextView) itemView.findViewById(R.id.tv_content);

        //展示评论
        recycler_comments = (RecyclerView) itemView.findViewById(R.id.recycler_comment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recycler_comments.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(context,null);
        recycler_comments.setAdapter(commentAdapter);

        // 展示九宫格图片
        rvImages = (RecyclerView) itemView.findViewById(R.id.rv_images);
        layoutManager = new GridLayoutManager(context, 3);
        rvImages.setLayoutManager(layoutManager);
        imageAdapter = new ImageAdapter(context, null);
        rvImages.setAdapter(imageAdapter);
    }

    // 刷新item布局中子控件的显示
    @Override
    protected void onRefreshView(Moment.MomentBean bean, int position) {
        // 显示用户名
        if (bean.getSender() == null) {
            tvUsername.setText("无名");
        } else {
            tvUsername.setText(bean.getSender().getUsername());

            // 显示用户昵称
            if (TextUtils.isEmpty(bean.getSender().getNick())) {
                tvNick.setVisibility(View.GONE);
            } else {
                tvNick.setVisibility(View.VISIBLE);
                tvNick.setText(bean.getSender().getNick());
            }

            // 显示头像
//            Glide.with(context).load(bean.getSender().getAvatar()).asBitmap().into(new SimpleTarget<Bitmap>() {
//
//                @Override
//                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
//                        glideAnimation) {
//                    ivAvatar.setImageBitmap(resource);
//                }
//            });
            ImagerLoaderUtil.getInstance(context).displayMyImage(bean.getSender().getAvatar(),
                    ivAvatar, new SimpleImageLoadingListener() {

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    ivAvatar.setImageBitmap(loadedImage);
                }
            });
        }

        // 显示朋友圈内容
        if (TextUtils.isEmpty(bean.getContent())) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setText(bean.getContent());
        }

        //显示评论
        int commentsCount = bean.getComments() == null ? 0 : bean.getComments().size();
        if (commentsCount == 0) {    //没有评论
            recycler_comments.setVisibility(View.GONE);
        } else {
            recycler_comments.setVisibility(View.VISIBLE);
            List<Moment.MomentBean.CommentsEntity> commentsEntityList = bean.getComments();
            commentAdapter.setDatas(commentsEntityList);
        }

        // 显示朋友圈图片
        int imageCount = bean.getImages() == null
                ? 0 : bean.getImages().size();
        if (imageCount == 0) {      // 没有朋友圈图片
            rvImages.setVisibility(View.GONE);
        } else {    // 有朋友圈图片
            rvImages.setVisibility(View.VISIBLE);
            List<String> images = new ArrayList<>();
            for (int i = 0; i < bean.getImages().size(); i++) {
                images.add(bean.getImages().get(i).getUrl());
            }
            imageAdapter.setDatas(images); // 刷新图片显示

            // 动态的指定图片宫格的宽高和RecyclerView的宽度
            // 1张图片 -> 1列
            // 4张图片 -> 2列
            // 其它    -> 3列
            ViewGroup.LayoutParams param = rvImages.getLayoutParams();
            if (imageCount == 1) {
                layoutManager.setSpanCount(1);
                param.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else if (imageCount == 4) {
                layoutManager.setSpanCount(2);
                // 两个图片宫格的宽度
                param.width = Global.getGridWidth() * 2;
            } else {        // 3列
                layoutManager.setSpanCount(3);
                param.width = ViewGroup.LayoutParams.MATCH_PARENT;
            }
        }
    }

}

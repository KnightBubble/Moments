package com.dc.moments.ui.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dc.moments.R;
import com.dc.moments.base.Global;
import com.dc.moments.ui.adapter.MyBaseAdapter;
import com.dc.moments.util.ImagerLoaderUtil;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by chenzhiwei on 17/11/16.
 */

public class ImageHolder extends MyBaseHolder<String> {

    private ImageView ivImage;

    public ImageHolder(Context context, ViewGroup parent,
                       MyBaseAdapter<String> adapter, int itemType) {
        super(context, parent, adapter, itemType, R.layout.item_image);
    }

    // 查找item中的子控件
    @Override
    public void onFindViews(View itemView) {
        ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
    }

    // 刷新item子控件的显示
    @Override
    protected void onRefreshView(String imagePath, int position) {
        // 动态设置图片宫格的宽高
        // 1张图片  ->   宫格的宽高为图片的宽高
        // 其它情况  ->  宫格的宽高为Global.getGridWidth()
        final ViewGroup.LayoutParams param = super.itemView.getLayoutParams();
        if (super.adapter.getItemCount() == 1) {    // 一张图片
//            Glide.with(context).load(imagePath).asBitmap().into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    // 指定宫格的宽高为图片的宽高
//                    param.width = resource.getWidth();
//                    param.height = resource.getHeight();
//                    // 显示图片
//                    ivImage.setImageBitmap(resource);
//                }
//            });
            ImagerLoaderUtil.getInstance(context).displayMyImage(imagePath,ivImage,new SimpleImageLoadingListener(){

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    param.width = loadedImage.getWidth();
                    param.height = loadedImage.getHeight();
                    ivImage.setImageBitmap(loadedImage);
                }
            });
        } else {    // 多张图片
            // 显示宫格图片
            int imageResId = Global.getResId(context, imagePath);
            ivImage.setBackgroundResource(imageResId);

            param.width = Global.getGridWidth();    // 指定宫格图片的宽
            param.height = Global.getGridWidth();
        }
    }

}

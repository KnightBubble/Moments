package com.dc.moments.util;

import android.content.Context;
import android.widget.ImageView;

import com.dc.moments.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by chenzhiwei on 17/11/17.
 */

public class ImagerLoaderUtil {

    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Context context;
    private static ImagerLoaderUtil imagerLoaderUtil;

    private ImagerLoaderUtil(Context context) {
        super();
        this.context = context;
    }

    public synchronized static ImagerLoaderUtil getInstance(Context context){
        if(imagerLoaderUtil == null){
            imagerLoaderUtil = new ImagerLoaderUtil(context);
            imagerLoaderUtil.initImageLoader();
        }
        return imagerLoaderUtil;
    }

    public void displayMyImage(String imageUrl, ImageView imageView ){
        imageLoader.displayImage(imageUrl, imageView);
    }

    public void displayMyImage(String imageUrl, ImageView imageView,SimpleImageLoadingListener listener ){
        imageLoader.displayImage(imageUrl, imageView, listener);
    }

    public void displayMyImage(String imageUrl, ImageView imageView, int resourceId){
        DisplayImageOptions tempOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(false)//设置下载的图片是否缓存在内存
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .showImageOnLoading(resourceId)         // 设置图片下载期间显示的图片
                .showImageForEmptyUri(resourceId)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(resourceId)       // 设置图片加载或解码过程中发生错误显示的图片
                .build();
        imageLoader.displayImage(imageUrl, imageView, tempOptions);
    }

    public void displayMyImage(String imageUrl, ImageView imageView, int resourceId, SimpleImageLoadingListener listener) {
        DisplayImageOptions tempOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(false)//设置下载的图片是否缓存在内存
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .showImageOnLoading(resourceId)         // 设置图片下载期间显示的图片
                .showImageForEmptyUri(resourceId)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(resourceId)       // 设置图片加载或解码过程中发生错误显示的图片
                .build();
        imageLoader.displayImage(imageUrl, imageView, tempOptions, listener);
    }

    public void initImageLoader() {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                //替换允许Https的图片加载
                .imageDownloader(new AuthImageDownloader(context))
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }
}


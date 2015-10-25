package com.example.ooo.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by ooo on 2015/10/25.
 */
public class PlaceHolderImageView extends ImageView {
    private ImageLoadingListener mImageLoadingListener;
    private DisplayImageOptions mOptions;
    private ScaleType mOriginalScaleType;

    public PlaceHolderImageView(Context context) {
        super(context);
        init();
    }

    public PlaceHolderImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public PlaceHolderImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        mOriginalScaleType = getScaleType();
        mImageLoadingListener = new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if (view instanceof PlaceHolderImageView) {
                    ((PlaceHolderImageView) view).setScaleType(ScaleType.CENTER);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (view instanceof PlaceHolderImageView) {
                    ((PlaceHolderImageView) view).setScaleType(ScaleType.CENTER);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (view instanceof PlaceHolderImageView) {
                    ((PlaceHolderImageView) view).setScaleType(mOriginalScaleType);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if (view instanceof PlaceHolderImageView) {
                    ((PlaceHolderImageView) view).setScaleType(ScaleType.CENTER);
                }
            }
        };

        mOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(android.R.drawable.stat_sys_headset)
                .showImageOnFail(android.R.drawable.stat_sys_headset)
                .showImageOnLoading(android.R.drawable.stat_sys_headset)
                .build();
    }

    public void setImageUrl(String url) {
        ImageLoader.getInstance().displayImage(url, this, mOptions, mImageLoadingListener);
    }

    public void setImageLoadingListener(ImageLoadingListener imageLoadingListener) {
        mImageLoadingListener = imageLoadingListener;
    }

    public void setOptions(DisplayImageOptions options) {
        mOptions = options;
    }
}

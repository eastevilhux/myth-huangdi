package com.star.light.common.local;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;

import com.bumptech.glide.request.target.ImageViewTarget;
import com.starlight.dot.framework.utils.SLog;

public class GlideRatioScaleTransForm extends ImageViewTarget<Bitmap> {
    private static final String TAG = "GlideRatioScaleTransForm==>";

    public GlideRatioScaleTransForm(ImageView view) {
        super(view);
    }

    @Override
    protected void setResource(@Nullable Bitmap resource) {
        if(resource == null){
            return;
        }
        view.setImageBitmap(resource);
        //获取原图的宽高
        int width = resource.getWidth();
        int height = resource.getHeight();
        SLog.INSTANCE.d(TAG,"width=>"+width+",height=>"+height);
        //获取imageView的宽
        int imageViewWidth = view.getWidth();

        //计算缩放比例
        float sy = (float) (imageViewWidth * 0.1) / (float) (width * 0.1);
        SLog.INSTANCE.d(TAG,"sy==>"+sy);
        //计算图片等比例放大后的高
        int imageViewHeight = (int) (height * sy);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = imageViewHeight;
        view.setLayoutParams(params);
    }
}

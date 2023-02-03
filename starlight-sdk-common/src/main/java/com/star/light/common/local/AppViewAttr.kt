package com.star.light.common.local

import android.database.sqlite.SQLiteOpenHelper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.star.light.common.R
import com.star.light.common.entity.AlbumImage
import com.star.light.common.entity.PopupItemEntity
import com.starlight.dot.framework.utils.dip2px
import com.starlight.dot.framework.utils.getScreenSize
import com.star.light.common.local.GlideApp
import com.star.light.common.view.AppImageView
import com.starlight.dot.framework.utils.SLog

private const val TAG = "AppViewAttr==>";

object AppViewAttr {
    private val circleCrop = CircleCrop();

    @JvmStatic
    @BindingAdapter("android:src")
    fun setCommonImage(view: ImageView, icon : AlbumImage?){
        when(view.id){
            R.id.iv_album_image->{
                icon?.let {
                    when(it.type){
                        1->{
                            Glide.with(view)
                                .asBitmap()
                                .load(icon.image)
                                .centerCrop()
                                .into(GlideRatioScaleTransForm(view))
                        }
                        3->{
                            Glide.with(view)
                                .asBitmap()
                                .load(icon.thumbnail)
                                .error(R.drawable.icon_video_error_default)
                                .placeholder(R.drawable.icon_video_error_default)
                                .into(GlideRatioScaleTransForm(view));
                        }
                        else-> view.setImageResource(icon.placeholderId);
                    }
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setCommonImage(view: ImageView, icon : PopupItemEntity){
        when(view.id){
            R.id.iv_popup_icon->{
                when(icon.iconType){
                    1->{
                        if(icon.iconResId != 0){
                            view.setImageResource(icon.iconResId);
                        }
                    }
                    2->{
                        GlideApp.with(view)
                            .load(icon.iconUrl)
                            .error(R.drawable.icon_item_no_data_default_grey)
                            .placeholder(R.drawable.icon_item_no_data_default_grey)
                            .into(view);
                    }
                    3->{
                        GlideApp.with(view)
                            .load(icon.iconFile)
                            .error(R.drawable.icon_item_no_data_default_grey)
                            .placeholder(R.drawable.icon_item_no_data_default_grey)
                            .into(view);
                    }
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter("app:src_url")
    fun setAppImageView(view: AppImageView, url : String?){
        when(view.srcStyle){
            AppImageView.SRC_STYLE_DEFAULT->{
                Glide.with(view)
                    .load(url)
                    .error(view.errorDrawable)
                    .placeholder(view.placeholderDrawable)
                    .into(view)
            }
            AppImageView.SRC_STYLE_FILLET->{
                SLog.d(TAG,"fillet==>");
                val filletRadius = view.filletRadius;
                val roundedCorners = RoundedCorners(filletRadius);
                Glide.with(view)
                    .load(url)
                    .optionalCenterCrop()
                    .apply(RequestOptions.bitmapTransform(roundedCorners))
                    .error(view.errorDrawable)
                    .placeholder(view.placeholderDrawable)
                    .into(view);
            }
            AppImageView.SRC_STYLE_CIRCLE->{
                Glide.with(view)
                    .load(url)
                    .transform(circleCrop)
                    .error(view.errorDrawable)
                    .placeholder(view.placeholderDrawable)
                    .into(view);
            }
        }
    }

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setVisibility(view : View,visibility : Boolean){
        when(view.id){
            else->{
                if(visibility){
                    view.visibility = View.VISIBLE;
                }else{
                    view.visibility = View.GONE;
                }
            }
        }
    }
}

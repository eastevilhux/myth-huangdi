package com.star.light.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.star.light.common.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;

@SuppressLint("AppCompatCustomView")
public class AppImageView extends ImageView {
    /**
     * 网络展示地址
     */
    private String url;

    /**
     * 文件类型展示地址, 使用前需要保证权限正常
     */
    private String filePath;

    /**
     * 资源文件
     */
    private Drawable resDrawable;
    /**
     * 错误展示文件
     */
    private Drawable errorDrawable;
    /**
     * 加载时展示文件
     */
    private Drawable placeholderDrawable;
    /**
     * src类型，
     */
    private int srcType;
    /**
     * 网络加载类型
     */
    public static final int SRC_TYPE_NETWORK = 1;
    /**
     * 本地资源类型
     */
    public static final int SRC_TYPE_RESOURCE = 2;
    /**
     * 文件类型
     */
    public static final int SRC_TYPE_FILE = 3;

    private int imgType;
    /**
     *  默认类型
     */
    public static final int IMG_TYPE_DEFAULT = 0;
    /**
     * banner展示类型
     */
    public static final int IMG_TYPE_BANNER = 1;
    /**
     * 用户图像类型
     */
    public static final int IMG_TYPE_ACCOUNT = 2;
    /**
     * 列表item展示类型
     */
    public static final int IMG_TYPE_LIST_ITEM = 3;
    /**
     * 矩形图片类型
     */
    public static final int IMG_TYPE_RECTANGLE = 4;

    /**
     * 圆角半径
     */
    private int filletRadius;
    private int srcStyle;
    /**
     * 默认
     */
    public static final int SRC_STYLE_DEFAULT = 0;
    /**
     * 圆角
     */
    public static final int SRC_STYLE_FILLET = 1;
    /**
     * 圆形
     */
    public static final int SRC_STYLE_CIRCLE = 2;

    public AppImageView(Context context) {
        super(context);
    }

    public AppImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AppImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public AppImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.AppImageView);
        srcType = ta.getInt(R.styleable.AppImageView_src_type,SRC_TYPE_RESOURCE);
        resDrawable = ta.getDrawable(R.styleable.AppImageView_src_res);
        errorDrawable = ta.getDrawable(R.styleable.AppImageView_error_src);
        placeholderDrawable = ta.getDrawable(R.styleable.AppImageView_placeholder_src);
        imgType = ta.getInt(R.styleable.AppImageView_img_type,IMG_TYPE_DEFAULT);
        url = ta.getString(R.styleable.AppImageView_src_url);
        filePath = ta.getString(R.styleable.AppImageView_src_file_path);
        filletRadius = ta.getInt(R.styleable.AppImageView_fillet_radius,10);
        srcStyle = ta.getInt(R.styleable.AppImageView_src_style,SRC_STYLE_FILLET);
        ta.recycle();
        initLoadingDrawable();
        switch (srcType){
            case SRC_TYPE_NETWORK:
                setUrl(url);
                break;
            case SRC_TYPE_RESOURCE:
                setResouceDrawable(resDrawable);
                break;
            case SRC_TYPE_FILE:
                setFile(filePath);
                break;
            default:
                break;
        }
    }

    /**
     * 设置网络图片展示地址
     * create by Eastevil at 2022/10/31 15:58
     * @author Eastevil
     * @param url
     *      网络图片展示地址
     * @return
     *      void
     */
    public void setUrl(String url){
        this.url = url;
        this.srcType = SRC_TYPE_NETWORK;
    }

    /**
     * 显示本地资源图片
     * create by Eastevil at 2022/10/31 15:59
     * @author Eastevil
     * @param resourceId
     *      本地图片资源id
     * @return
     *      void
     */
    public void setResouce(int resourceId){
        setResouceDrawable(ContextCompat.getDrawable(getContext(),resourceId));
    }

    private void setResouceDrawable(Drawable resouceDrawable){
        this.resDrawable = resouceDrawable;
        this.srcType = SRC_TYPE_RESOURCE;
    }

    /**
     * 显示文件类型图片
     * create by Eastevil at 2022/10/31 16:02
     * @author Eastevil
     * @param filePath
     *      文件路径
     * @return
     *      void
     */
    public void setFile(String filePath){
        this.filePath = filePath;
        this.srcType = SRC_TYPE_FILE;
    }

    /**
     * 设置加载错误误图片占位图片
     * create by Eastevil at 2022/10/31 16:03
     * @author Eastevil
     * @param errorId
     *      错误图片资源id
     * @return
     *      void
     */
    public void error(int errorId){
        errorDrawable = ContextCompat.getDrawable(getContext(),errorId);
    }

    /**
     * 设置加载时占位图片
     * create by Eastevil at 2022/10/31 16:04
     * @author Eastevil
     * @param placeholderId
     *      图片资源文件id
     * @return
     *      void
     */
    public void placeholder(int placeholderId){
        placeholderDrawable = ContextCompat.getDrawable(getContext(),placeholderId);
    }

    /**
     * 初始化加载时和加载错误时的占位图片
     * create by Eastevil at 2022/10/31 15:55
     * @author Eastevil
     * @return
     *      void
     */
    private void initLoadingDrawable(){
        //已定义的错误展示图片为最高优先级，如果为空，则表示未定义，通过类型来增加默认错误
        switch (imgType){
            case IMG_TYPE_BANNER:
                if(errorDrawable == null){
                    errorDrawable = ContextCompat.getDrawable(getContext(),R.drawable.banner_bg_default_grey);
                }
                if(placeholderDrawable == null){
                    placeholderDrawable = ContextCompat.getDrawable(getContext(),R.drawable.banner_bg_default_grey);
                }
                break;
            case IMG_TYPE_ACCOUNT:
                if(errorDrawable == null){
                    errorDrawable = ContextCompat.getDrawable(getContext(),R.drawable.icon_account_noimg_default_grey);
                }
                if(placeholderDrawable == null){
                    placeholderDrawable = ContextCompat.getDrawable(getContext(),R.drawable.icon_account_noimg_default_grey);
                }
                break;
            case IMG_TYPE_LIST_ITEM:
                if(errorDrawable == null){
                    errorDrawable = ContextCompat.getDrawable(getContext(),R.drawable.icon_item_no_data_default_grey);
                }
                if(placeholderDrawable == null){
                    placeholderDrawable = ContextCompat.getDrawable(getContext(),R.drawable.icon_item_no_data_default_grey);
                }
                break;
            case IMG_TYPE_RECTANGLE:
                if(errorDrawable == null){
                    errorDrawable = ContextCompat.getDrawable(getContext(),R.drawable.icon_rectangle_no_data_default);
                }
                if(placeholderDrawable == null){
                    placeholderDrawable = ContextCompat.getDrawable(getContext(),R.drawable.icon_rectangle_no_data_default);
                }
                break;
            default:
                if(errorDrawable == null){
                    errorDrawable = ContextCompat.getDrawable(getContext(),R.drawable.common_noimg_default_rectangle);
                }
                if(placeholderDrawable == null){
                    placeholderDrawable = ContextCompat.getDrawable(getContext(),R.drawable.common_noimg_default_rectangle);
                }
                break;
        }
    }

    public Drawable getErrorDrawable() {
        return errorDrawable;
    }

    public Drawable getPlaceholderDrawable() {
        return placeholderDrawable;
    }

    public Drawable getResDrawable() {
        return resDrawable;
    }

    public String getUrl() {
        return url;
    }

    public int getImgType() {
        return imgType;
    }

    public int getSrcType() {
        return srcType;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getFilletRadius() {
        return filletRadius;
    }

    public int getSrcStyle() {
        return srcStyle;
    }
}

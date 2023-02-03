package com.star.light.common.entity

import android.content.Context
import android.graphics.Color
import com.star.light.common.model.image.ImageActivity
import com.star.light.album.model.video.VideoActivity
import com.star.light.common.model.image.ImageData
import com.starlight.dot.framework.local.EastRouter
import com.starlight.dot.framework.utils.toJSON
import java.lang.NullPointerException

class AlbumData {

    companion object{
        private const val REQUEST_ALBUM_CODE = 800;
        const val DATA_TYPE_IMG = 0;
        const val DATA_TYPE_VIDEO = 1;
    }

    var title : String = "alubm list";
    var rationaleAlubmText : String = "app request alubm premission";
    var rationaleCamearText : String = "app request camera premission";
    var deniedAlbumPermissionText : String = "alubm premission is denied";
    var deniedCameraPermissionText : String = "camera premission is denied";
    var requestCode : Int = REQUEST_ALBUM_CODE;
    var maxNum : Int = 1;
    var iconSelectColor : Int = 0;
    var iconNormalColor : Int = 0;
    var titleColor : Int = Color.WHITE;
    var needZoom : Boolean = true;
    var zoomWidth : Float = 1080F;
    var zoomPath : String = "/star/light/tempimg"
    var dataType : Int = DATA_TYPE_IMG;

    constructor(builder : Builder){
        this.title = builder.title;
        this.rationaleAlubmText = builder.rationaleAlubmText;
        this.rationaleCamearText = builder.rationaleCamearText;
        this.requestCode = builder.requestCode;
        this.deniedAlbumPermissionText = builder.deniedAlbumPermissionText;
        this.deniedCameraPermissionText = builder.deniedCameraPermissionText;
        this.maxNum = builder.maxNum;
        this.iconSelectColor = builder.iconSelectColor;
        this.iconNormalColor = builder.iconNormalColor;
        this.titleColor = builder.titleColor;
        this.needZoom = builder.needZoom;
        this.zoomWidth = builder.zoomWidth;
        this.zoomPath = builder.zoomPath;
        this.dataType = builder.dataType;
    }

    class Builder{
        internal var title : String = "alubm list";
        internal var rationaleAlubmText : String = "app request alubm premission";
        internal var rationaleCamearText : String = "app request camera premission";
        internal var requestCode : Int = ImageData.REQEUST_ALBUM_CODE;
        internal var deniedAlbumPermissionText : String = "album premission is not allow";
        internal var deniedCameraPermissionText : String = "camera premission is denied";
        internal var maxNum : Int = 1;
        internal var iconSelectColor : Int = 0;
        internal var iconNormalColor : Int = 0;
        internal var titleColor : Int = Color.WHITE;
        internal var needZoom : Boolean = true;
        internal var zoomWidth : Float = 1080F;
        internal var zoomPath : String = "/star/light/tempimg"
        internal var dataType : Int = DATA_TYPE_IMG;

        fun title(title : String): Builder {
            this.title = title;
            return this;
        }

        fun rationaleAlubmText(rationaleAlubmText : String): Builder {
            this.rationaleAlubmText = rationaleAlubmText;
            return this;
        }

        fun rationaleCamearText(rationaleCamearText : String): Builder {
            this.rationaleCamearText = rationaleCamearText;
            return this;
        }

        fun requestCode(requestCode : Int): Builder {
            this.requestCode = requestCode;
            return this;
        }

        fun deniedAlbumPermissionText(deniedAlbumPermissionText : String): Builder {
            this.deniedAlbumPermissionText = deniedAlbumPermissionText;
            return this;
        }

        fun deniedCameraPermissionText(deniedCameraPermissionText : String): Builder {
            this.deniedCameraPermissionText = deniedCameraPermissionText;
            return this;
        }

        fun maxNum(maxNum : Int): Builder {
            this.maxNum = maxNum;
            return this;
        }

        fun iconSelectColor(iconSelectColor : Int): Builder {
            this.iconSelectColor = iconSelectColor;
            return this;
        }

        fun iconNormalColor(iconNormalColor : Int): Builder {
            this.iconNormalColor = iconNormalColor;
            return this;
        }

        fun titleColor(titleColor : Int): Builder {
            this.titleColor = titleColor;
            return this;
        }

        fun needZoom(needZoom : Boolean): Builder {
            this.needZoom = needZoom;
            return this;
        }

        fun zoomWidth(zoomWidth : Float): Builder {
            this.zoomWidth = zoomWidth;
            return this;
        }

        fun zoomPath(zoomPath : String): Builder {
            this.zoomPath = zoomPath;
            return this;
        }

        fun builder(): AlbumData {
            return AlbumData(this);
        }

        fun dataType(dataType : Int): Builder {
            this.dataType = dataType;
            return this;
        }
    }

    fun requestAlubm(context: Context, isFinish :  Boolean = false){
        val requestData =
            this.toJSON() ?: throw NullPointerException("request data is not allow null");
        if(dataType == DATA_TYPE_IMG){
            EastRouter.with(context)
                .target(ImageActivity::class.java)
                .addParam(ImageData.FRAGMENT_IMAGE_KEY, ImageData.FragmentKey.FRAGMENT_IMAGE_LIST)
                .addParam(ImageData.DATA_IMAGE_KEY,requestData)
                .addParam(ImageData.ALBUM_TITLE_COLOR_KEY,titleColor)
                .requestCode(requestCode)
                .isFinish(isFinish)
                .start();
        }else if(dataType == DATA_TYPE_VIDEO){
            EastRouter.with(context)
                .target(VideoActivity::class.java)
                .addParam(ImageData.DATA_IMAGE_KEY,requestData)
                .addParam(ImageData.ALBUM_TITLE_COLOR_KEY,titleColor)
                .requestCode(requestCode)
                .isFinish(isFinish)
                .start();
        }
    }
}

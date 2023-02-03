package com.star.light.common.entity

import android.graphics.Bitmap
import com.bumptech.glide.Glide
import java.io.File

class AlbumImage {
    companion object{
        private const val TIME_UNIT_MINUTE = 3600 * 60;
    }

    var image : File? = null;

    /**
     * 0:默认占位
     * 1:图片
     * 2:相机拍照
     * 3:视频
     */
    var type : Int = 1;

    var placeholderId : Int = 0;

    var select : Boolean = false;

    /**
     * 视频文件
     */
    var videoPath : String? = null;

    /**
     * 视频封面图片
     */
    var thumbnail : Bitmap? = null;

    var videoTime : Long = 0;

    var videoSize : Long = 0;

    val videoTimeText : String
    get() {
        return videoTime(videoTime);
    }

    private fun videoTime(time : Long): String {
        if(time <= 0){
            return "00:00";
        }
        //分
        val minutes = if(time < 1000){
            0
        }else{
            (time/1000)/60;
        }
        val mTime = if(minutes <= 0){
            "00";
        }else if(minutes in 1..9){
            "0${minutes}"
        }else{
            minutes.toString();
        }
        //秒
        val remainingSeconds = (time/1000) % 60
        val rTime = if(remainingSeconds <= 0){
            "00";
        }else if(remainingSeconds in 1..9){
            "0${remainingSeconds}"
        }else{
            remainingSeconds.toString();
        }
        return "${mTime}:${rTime}";
    }
}

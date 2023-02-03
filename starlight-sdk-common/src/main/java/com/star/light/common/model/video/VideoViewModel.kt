package com.star.light.common.model.video

import android.app.Application
import android.content.ContentResolver
import android.database.Cursor
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import com.star.light.common.AppViewModel
import com.star.light.common.R
import com.star.light.common.entity.AlbumData
import com.star.light.common.entity.AlbumImage
import com.starlight.dot.framework.utils.mainThread
import com.starlight.dot.framework.utils.parseJSON
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class VideoViewModel(application: Application) : AppViewModel<VideoData>(application) {
    private lateinit var imageData : AlbumData;

    companion object{
        private const val TAG = "SL_VideoViewModel==>";

        /**
         * app所需要得权限
         */
        val APP_PERMISSIONS = arrayOf(
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.RECORD_AUDIO
        )

        var progress = arrayOf<String>(
            MediaStore.Video.Media.DISPLAY_NAME,  //视频的名字
            MediaStore.Video.Media.SIZE,  //大小
            MediaStore.Video.Media.DURATION,  //长度
            MediaStore.Video.Media.DATA
        )
    }

    val videoList = MutableLiveData<MutableList<AlbumImage>>();
    val camearUri = MutableLiveData<Uri>();

    /**
     * 选择结束时选择的文件路径,如果需要压缩，则将存放压缩后的图片文件地址
     */
    val pathList = MutableLiveData<MutableList<String>>();

    override fun initData(): VideoData {
        return VideoData();
    }

    fun initImageData(data : String){
        imageData = data.parseJSON();
    }

    fun imageData(): AlbumData {
        return imageData;
    }

    override fun onResume() {
        super.onResume()
    }

    fun videoList() = GlobalScope.launch{
        showLoading();
        val result = readVideoList();
        val pImg = AlbumImage();
        pImg.type = 2;
        pImg.placeholderId = R.drawable.ic_camera;
        result.add(0,pImg);
        dismissLoading();
        if(result.isNotEmpty()){
            mainThread {
                videoList.value = result;
            }
        }
    }

    suspend private fun readVideoList() = withContext(Dispatchers.IO){
        val app = getApplication<Application>();
        val list = mutableListOf<AlbumImage>();
        val cr: ContentResolver = app.getContentResolver();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            val cursor : Cursor? = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, progress,
                null, null, null);
            cursor?.let {
                while (it.moveToNext()){
                    val name = it.getString(0) //得到视频的名字
                    val size = it.getLong(1) //得到视频的大小
                    val durantion = it.getLong(2) //得到视频的时间长度
                    val data = it.getString(3) //得到视频的路径，可以转化为uri进行视频播放
                    val thumbnail: Bitmap? = ThumbnailUtils.createVideoThumbnail(data,MediaStore.Video.Thumbnails.MINI_KIND);
                    val albumImage = AlbumImage();
                    albumImage.thumbnail = thumbnail;
                    albumImage.image = File(data);
                    albumImage.select = false;
                    albumImage.type = 3;
                    albumImage.videoTime = durantion;
                    albumImage.videoPath = data;
                    albumImage.videoSize = size;
                    list.add(albumImage);
                }
                it.close();
            }
        }
        //不论是否有sd卡都要查询手机内存
        val cursor = cr.query(MediaStore.Video.Media.INTERNAL_CONTENT_URI, progress, null,
            null, null)
        cursor?.let {
            while (it.moveToNext()){
                val name = it.getString(0) //得到视频的名字
                val size = it.getLong(1) //得到视频的大小
                val durantion = it.getLong(2) //得到视频的时间长度
                val data = it.getString(3) //得到视频的路径，可以转化为uri进行视频播放
                val thumbnail: Bitmap? = ThumbnailUtils.createVideoThumbnail(data,MediaStore.Video.Thumbnails.MINI_KIND);
                val albumImage = AlbumImage();
                albumImage.thumbnail = thumbnail;
                albumImage.image = File(data);
                albumImage.select = false;
                albumImage.type = 3;
                albumImage.videoTime = durantion;
                albumImage.videoPath = data;
                albumImage.videoSize = size;
                list.add(albumImage);
            }
            it.close();
        }
        return@withContext list;
    }


}

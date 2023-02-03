package com.star.light.common.model.image.list

import android.app.Application
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.core.os.EnvironmentCompat
import androidx.lifecycle.MutableLiveData
import com.star.light.common.ext.compressSize
import com.star.light.common.AppViewModel
import com.star.light.common.R
import com.star.light.common.entity.AlbumData
import com.star.light.common.entity.AlbumImage
import com.star.light.common.model.image.ImageData
import com.starlight.dot.framework.utils.mainThread
import com.starlight.dot.framework.utils.parseJSON
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ListViewModel(application: Application) : AppViewModel<ImageData>(application) {

    companion object{
        private const val TAG = "SL_ABLUM_ListViewModel==>"

        val PARAMS_IMAGE = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID
        );
    }

    private lateinit var imageData : AlbumData;
    val albumImageList = MutableLiveData<MutableList<AlbumImage>>();
    val camearUri = MutableLiveData<Uri>();

    /**
     * 选择结束时选择的文件路径,如果需要压缩，则将存放压缩后的图片文件地址
     */
    val pathList = MutableLiveData<MutableList<String>>();

    override fun initData(): ImageData {
        return ImageData();
    }

    fun initImageData(data : String){
        imageData = data.parseJSON();
    }

    fun imageData(): AlbumData {
        return imageData;
    }

    fun queryImage() = GlobalScope.launch{
        showLoading();
        val list = readImageList();
        dismissLoading();
        if(list.isNotEmpty()){
            val pImg = AlbumImage();
            pImg.type = 2;
            pImg.placeholderId = R.drawable.ic_camera;
            list.add(0,pImg);
            mainThread {
                albumImageList.value = list;
            }
        }
    }

    suspend fun readImageList() = withContext(Dispatchers.IO){
        val application = getApplication<Application>();
        val mCursor: Cursor? = application.getContentResolver().query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
            null, null, MediaStore.Images.Media._ID +" desc"
        )
        val list = mutableListOf<AlbumImage>();
        mCursor?.let {
            while (it.moveToNext()){
                //获取图片的名称
                val imgName = it.getString(it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                // 获取图片的绝对路径
                val column_index = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                val path = it.getString(column_index);
                val f = File(path);
                if(f.length() > 0){
                    val albumImg = AlbumImage();
                    albumImg.type = 1;
                    albumImg.image = f
                    albumImg.select = false;
                    list.add(albumImg);
                }
            }
        }
        mCursor?.close();
        return@withContext list;
    }

    fun takePhoto(){
        camearUri.value = createCameraUri();
    }

    /**
     * 图片选择结束
     * create by Eastevil at 2022/7/20 13:35
     * @author Eastevil
     * @param pathList
     *      选择得图片文件
     * @return
     */
    fun selectFinishe(pathList : MutableList<String>) = GlobalScope.launch{
        val dirPath = "${getApplication<Application>().getExternalCacheDir()?.getPath()}${imageData.zoomPath}"
        if(imageData.needZoom){
            showLoading();
            val list = mutableListOf<String>();
            pathList.forEach {
                val file = File(it);
                val f = file.compressSize(imageData.zoomWidth,dirPath,file.path);
                f?.let {
                    list.add(it.path);
                }
            }
            dismissLoading();
            mainThread {
                this@ListViewModel.pathList.value = list;
            }
        }else{
            mainThread {
                this@ListViewModel.pathList.value = pathList;
            }
        }
    }

    private fun createCameraUri() : Uri? {
        val application = getApplication<Application>();
        var camearUri : Uri? = null;
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q){
            //android Q以上
            val status: String = Environment.getExternalStorageState()
            // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
            camearUri =  if (status == Environment.MEDIA_MOUNTED) {
                application.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    ContentValues()
                )
            } else {
                application.getContentResolver().insert(
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                    ContentValues()
                )
            }
        }else{
            val file = createImageFile();
            if(file != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                    camearUri = FileProvider.getUriForFile(application, application.getPackageName() + ".fileprovider", file);
                }else{
                    camearUri = Uri.fromFile(file);
                }
            }
        }
        return camearUri;
    }


    /**
     * 创建保存图片的文件
     */
    private fun createImageFile(): File? {
        val application = getApplication<Application>();
        val imageName: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (storageDir?.exists() == false) {
            storageDir?.mkdir()
        }
        val tempFile = File(storageDir, imageName)
        return if (Environment.MEDIA_MOUNTED != EnvironmentCompat.getStorageState(tempFile)) {
            null
        } else tempFile
    }
}

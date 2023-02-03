package com.star.light.album.model.video

import android.content.Intent
import android.provider.MediaStore
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.star.light.common.AppActivity
import com.star.light.common.R
import com.star.light.common.adapter.AlbumImageAdapter
import com.star.light.common.databinding.ActivitySlalbumVideoBinding
import com.star.light.common.local.GridSpaceItemDecoration
import com.star.light.common.model.image.ImageData
import com.star.light.common.model.video.VideoViewModel
import com.starlight.dot.framework.utils.dip2px
import com.starlight.dot.framework.utils.toJSON
import pub.devrel.easypermissions.EasyPermissions

class VideoActivity : AppActivity<ActivitySlalbumVideoBinding, VideoViewModel>(),EasyPermissions.PermissionCallbacks {

    private lateinit var adapter : AlbumImageAdapter;

    override fun getLayoutRes(): Int {
        return R.layout.activity_slalbum_video;
    }

    override fun getVMClass(): Class<VideoViewModel> {
        return VideoViewModel::class.java;
    }

    override fun initView() {
        super.initView()
        val titleColor = intent.getIntExtra(
            ImageData.ALBUM_TITLE_COLOR_KEY,
            getColor(R.color.slColorMainTheme));
        dataBinding.slTitleLayout.setBackgroundColor(titleColor);

        val imageData = intent.getStringExtra(ImageData.DATA_IMAGE_KEY);
        if(imageData == null){
            setResult(ImageData.RESULT_CODE_STAR_ALBUM);
            finish();
            return;
        }
        viewModel.initImageData(imageData);

        //判断是否拥有权限
        val havePer = EasyPermissions.hasPermissions(this,*VideoViewModel.APP_PERMISSIONS);
        if(!havePer){
            //没有权限，请求权限
            EasyPermissions.requestPermissions(this,viewModel.imageData().rationaleAlubmText,
                ImageData.REQEUST_ALBUM_PERMISSION_CODE,*VideoViewModel.APP_PERMISSIONS);
        }else{
            //已有权限，读取视频信息
            viewModel.videoList();
        }

        adapter = AlbumImageAdapter(this,null);
        adapter.setMaxNum(1);
        dataBinding.adapter = adapter;

        adapter.onCamera {
            if(!EasyPermissions.hasPermissions(this,*VideoViewModel.APP_PERMISSIONS)){
                EasyPermissions.requestPermissions(this,viewModel.imageData().rationaleCamearText,
                    ImageData.REQUEST_CAMERA_PERMISSION_CODE,*VideoViewModel.APP_PERMISSIONS);
            }else{
                takeVideo();
            }
        }

        adapter.onVideoSelect {
            //选择结束
            val intent = Intent();
            it.toJSON()?.let {
                intent.putExtra(ImageData.VIDEO_DATA,it);
            }
            setResult(ImageData.RESULT_CODE_VIDEO,intent);
            finish();
        }
        dataBinding.rvVideo.layoutManager = GridLayoutManager(this,4);
        dataBinding.rvVideo.addItemDecoration(GridSpaceItemDecoration(4,1.dip2px(),1.dip2px()));
    }

    override fun addObserve() {
        super.addObserve()
        viewModel.videoList.observe(this, Observer {
            adapter.dataList = it;
            adapter.notifyDataSetChanged();
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //将请求结果传递EasyPermission库处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
       /* if(requestCode == ImageData.REQEUST_ALBUM_PERMISSION_CODE){
            showToastShort(viewModel.imageData().deniedAlbumPermissionText);
        }else if(requestCode == ImageData.REQUEST_CAMERA_PERMISSION_CODE){
            showToastShort(viewModel.imageData().deniedCameraPermissionText);
        }*/
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if(requestCode == ImageData.REQEUST_ALBUM_PERMISSION_CODE){
            if(EasyPermissions.hasPermissions(this,*perms.toTypedArray())){
                viewModel.videoList()
            }else{
                showToastShort(viewModel.imageData().deniedAlbumPermissionText);
            }
        }else if(requestCode == ImageData.REQUEST_CAMERA_PERMISSION_CODE){
            if(EasyPermissions.hasPermissions(this,*perms.toTypedArray())){
                //调用系统相机
                takeVideo();
            }else{
                showToastShort(viewModel.imageData().deniedCameraPermissionText);
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ImageData.REQUEST_VIDEO_CODE){
            //跳转进入拍照返回,重新刷新相册数据
            viewModel.videoList();
        }
    }

    private fun takeVideo(){
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takeVideoIntent, ImageData.REQUEST_VIDEO_CODE)
            }
        }
    }

    companion object{
        private const val TAG = "VideoActivity==>"
    }

}

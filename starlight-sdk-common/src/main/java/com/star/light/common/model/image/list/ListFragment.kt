package com.star.light.common.model.image.list

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.star.light.common.model.image.ImageActivity
import com.star.light.common.AppFragment
import com.star.light.common.Constants
import com.star.light.common.R
import com.star.light.common.adapter.AlbumImageAdapter
import com.star.light.common.databinding.FragmentImageListBinding
import com.star.light.common.local.GridSpaceItemDecoration
import com.star.light.common.model.image.ImageData
import com.starlight.dot.framework.utils.dip2px
import com.starlight.dot.framework.utils.toJSON
import pub.devrel.easypermissions.EasyPermissions

class ListFragment : AppFragment<FragmentImageListBinding, ListViewModel>(), EasyPermissions.PermissionCallbacks {
    private lateinit var adapter : AlbumImageAdapter;

    override fun getLayoutRes(): Int {
        return R.layout.fragment_image_list;
    }

    override fun getVMClass(): Class<ListViewModel> {
        return ListViewModel::class.java;
    }

    override fun initView() {
        super.initView()

        val imageData = arguments?.getString(ImageData.DATA_IMAGE_KEY);
        if(imageData == null){
            throw NullPointerException("image data is null");
        }
        viewModel.initImageData(imageData);

        val havePer = EasyPermissions.hasPermissions(requireContext(),*Constants.AlubmPermission.PERMISS_ALUBM);
        if(!havePer){
            //没有权限，请求权限
            EasyPermissions.requestPermissions(this,viewModel.imageData().rationaleAlubmText,
                ImageData.REQEUST_ALBUM_PERMISSION_CODE,*Constants.AlubmPermission.PERMISS_ALUBM);
        }else{
            //已有权限，读取相册信息
            viewModel.queryImage();
        }

        adapter = AlbumImageAdapter(requireContext(),null);
        adapter.setMaxNum(viewModel.imageData().maxNum)
        dataBinding.adapter = adapter;

        adapter.onCamera {
            if(!EasyPermissions.hasPermissions(requireContext(),*Constants.AlubmPermission.PERMISS_CAMERA)){
                EasyPermissions.requestPermissions(this,viewModel.imageData().rationaleCamearText,
                    ImageData.REQUEST_CAMERA_PERMISSION_CODE,*Constants.AlubmPermission.PERMISS_CAMERA);
            }else{
                toCamera();
            }
        }

        adapter.onSelectFinish {
            //选择结束
            viewModel.selectFinishe(it);
        }
        dataBinding.rvAlbumImage.layoutManager = GridLayoutManager(requireContext(),4);
        dataBinding.rvAlbumImage.addItemDecoration(GridSpaceItemDecoration(4,1.dip2px(),1.dip2px()));
    }

    override fun addObserve() {
        super.addObserve()
        viewModel.albumImageList.observe(this, Observer {
            adapter.dataList = it;
            adapter.notifyDataSetChanged();
        })

        viewModel.camearUri.observe(this, Observer {
            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, it);
            captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(captureIntent, ImageData.REQUEST_CAMERA_CODE);
        })

        viewModel.pathList.observe(this, Observer {
            val listJson = it.toJSON();
            listJson?.let {
                val ac = getAppActivity(ImageActivity::class.java);
                val intent = Intent();
                intent.putExtra(ImageData.STAR_IMAGE_PATH_LIST,it);
                ac?.setResult(ImageData.RESULT_CODE_STAR_ALBUM,intent);
                ac?.finish();
           }
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
        if(requestCode == ImageData.REQEUST_ALBUM_PERMISSION_CODE){
            showToastShort(viewModel.imageData().deniedAlbumPermissionText);
        }else if(requestCode == ImageData.REQUEST_CAMERA_PERMISSION_CODE){
            showToastShort(viewModel.imageData().deniedCameraPermissionText);
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if(requestCode == ImageData.REQEUST_ALBUM_PERMISSION_CODE){
            if(EasyPermissions.hasPermissions(requireContext(),*perms.toTypedArray())){
                viewModel.queryImage();
            }else{
                showToastShort(viewModel.imageData().deniedAlbumPermissionText);
            }
        }else if(requestCode == ImageData.REQUEST_CAMERA_PERMISSION_CODE){
            if(EasyPermissions.hasPermissions(requireContext(),*perms.toTypedArray())){
                //调用系统相机
                toCamera();
            }else{
                showToastShort(viewModel.imageData().deniedCameraPermissionText);
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ImageData.REQUEST_CAMERA_CODE){
            //跳转进入拍照返回,重新刷新相册数据
            viewModel.queryImage();
        }
    }

    private fun toCamera(){
        viewModel.takePhoto();
    }

    override fun onMenuTextClick(menuTextView: TextView) {
        super.onMenuTextClick(menuTextView)
        adapter.selectFinish();
    }

    companion object{
        private const val TAG = "Image_ListFragment==>";

        fun newInstance(): ListFragment {
            val args = Bundle()
            val fragment = ListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

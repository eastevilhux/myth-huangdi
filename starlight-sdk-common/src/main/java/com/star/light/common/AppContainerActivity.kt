package com.star.light.common

import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import com.star.light.common.model.image.ImageData
import com.starlight.dot.framework.commons.ContainerActivity
import com.starlight.dot.framework.utils.toJsonList
import com.starlight.dot.framework.widget.TitleLayout

abstract class AppContainerActivity<V : AppViewModel<*>> : ContainerActivity<V>() {

    override fun initTitle(key: String, titleLayout: TitleLayout) {
        super.initTitle(key, titleLayout)
        titleLayout.setBackgroundColor(getColor(R.color.slColorMainTheme))
        titleLayout.setOnTitleLayoutListener(onTitleLayoutListener);
        titleLayout.setTitleTextColor(getColor(R.color.colorWhite));
        titleLayout.setTitleTextSize(18);
        titleLayout.setBackType(TitleLayout.BackType.TYPE_IMAGE);
        titleLayout.setBackIcon(R.drawable.icon_sl_back);
    }

    private val onTitleLayoutListener = object : TitleLayout.OnTitleLayoutListener {

        override fun onBackIconClickListener(menuImageView: ImageView) {
            back();
            this@AppContainerActivity.onBackIconClickListener(menuImageView);
        }

        override fun onBackTextClickListener(backTextView: TextView) {
            this@AppContainerActivity.onBackTextClickListener(backTextView);
        }

        override fun onMenuIconClickListener(menuImageView: ImageView) {
            this@AppContainerActivity.onMenuIconClickListener(menuImageView);
        }

        override fun onMenuTextClickListener(menuTextView: TextView) {
            this@AppContainerActivity.onMenuTextClickListener(menuTextView);
        }

        override fun onSubTitleClickListener(subTitleView: TextView) {
            this@AppContainerActivity.onSubTitleClickListener(subTitleView);
        }

        override fun onTitleClickListener(titleTextView: TextView) {
            this@AppContainerActivity.onTitleClickListener(titleTextView);
        }
    }

    open fun onBackIconClickListener(menuImageView: ImageView){
        val f = showKey?.let { getFragment(it) };
        if(f is AppFragment){
            f.onBackIconClick(menuImageView);
        }
    }

    open fun onBackTextClickListener(backTextView: TextView) {
        val f = showKey?.let { getFragment(it) };
        if(f is AppFragment){
            f.onBackTextClick(backTextView);
        }
    }

    open fun onMenuIconClickListener(menuImageView: ImageView) {
        val f = showKey?.let { getFragment(it) };
        if(f is AppFragment){
            f.onMenuIconClick(menuImageView);
        }
    }

    open fun onMenuTextClickListener(menuTextView: TextView) {
        val f = showKey?.let { getFragment(it) };
        if(f is AppFragment){
            f.onMenuTextClick(menuTextView);
        }
    }

    open fun onSubTitleClickListener(subTitleView: TextView) {
        val f = showKey?.let { getFragment(it) };
        if(f is AppFragment){
            f.onSubTitleClick(subTitleView);
        }
    }

    open fun onTitleClickListener(titleTextView: TextView) {
        val f = showKey?.let { getFragment(it) };
        if(f is AppFragment){
            f.onTitleClick(titleTextView);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == ImageData.RESULT_CODE_STAR_ALBUM){
            //由相册模块儿返回
            data?.let {
                val pathListData = it.getStringExtra(ImageData.STAR_IMAGE_PATH_LIST);
                pathListData?.let {
                    val pList = it.toJsonList<String>().toMutableList();
                    onAlbumSelect(requestCode,pList);
                }?:let {
                    onAlbumSelect(requestCode,null);
                }
            }
        } else if(resultCode == ImageData.RESULT_CODE_VIDEO){
            data?.let {
                val path = it.getStringExtra(ImageData.VIDEO_DATA);
                onVideoSelect(requestCode,path);
            }
        } else {
            if (requestCode == ImageData.REQUEST_STAR_CUTTING) {
                onCuttingResult();
            } else {
                when (resultCode) {

                }
            }
        }
    }

    open fun onCuttingResult(){
        val fragment = showKey?.let { getFragment(it) };
        if(fragment is AppFragment){
            fragment.onCuttingResult();
        }
    }

    open fun onAlbumSelect(requestCode : Int,pathList : MutableList<String>? = null){
        val fragment = showKey?.let { getFragment(it) };
        if(fragment is AppFragment){
            fragment.onAlbumSelect(requestCode,pathList);
        }
    }

    open fun onVideoSelect(requestCode : Int,videoData : String?){
        val fragment = showKey?.let { getFragment(it) };
        if(fragment is AppFragment){
            fragment.onVideoSelect(requestCode,videoData);
        }
    }

}

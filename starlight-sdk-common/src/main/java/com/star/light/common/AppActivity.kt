package com.star.light.common

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.star.light.common.model.image.ImageData
import com.starlight.dot.framework.entity.Loading
import com.starlight.dot.framework.commons.EastLessOnScrollListener
import com.starlight.dot.framework.commons.SDKActivity
import com.starlight.dot.framework.entity.VMData
import com.starlight.dot.framework.utils.toJsonList
import com.starlight.dot.framework.widget.LoadingDialog
import com.starlight.dot.framework.widget.TitleLayout
import com.starlight.dot.framework.widget.ViewToast

abstract class AppActivity<D : ViewDataBinding, V : AppViewModel<*>> : SDKActivity<D, V>(),TitleLayout.OnTitleLayoutListener{

    companion object{
        const val TAG = "LYNFREE_AppActivity==>"
    }

    private var bssLoading : LoadingDialog? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onVmdataError(data: VMData) {
        when(data.error.erroCode){
            ConfigureHelper.LOGIN_ERROR -> toLogin()
            ConfigureHelper.LOGIN_TOKEN_ERROR -> toLogin()
            ConfigureHelper.AUTH_ERROR -> toLogin();
            ConfigureHelper.TOKEN_INVALID-> toLogin();
            else->onError(data);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG,"onActivityResult_requestCode=>${requestCode},resultCode=>${resultCode}");
        if(resultCode == ImageData.RESULT_CODE_STAR_ALBUM){
            //由相册模块儿返回
            data?.let {
                val pathListData = it.getStringExtra(ImageData.STAR_IMAGE_PATH_LIST);
                pathListData?.let {
                    val pList = it.toJsonList<String>();
                    onAlbumSelect(requestCode,pList.toMutableList());
                }?:let {
                    onAlbumSelect(requestCode,null);
                }
            }
        }else if(resultCode == ImageData.RESULT_CODE_VIDEO){
            data?.let {
                val path = it.getStringExtra(ImageData.VIDEO_DATA);
                onVideoSelect(requestCode,path);
            }
        } else{
            if (requestCode == ImageData.REQUEST_STAR_CUTTING){
                onCuttingResult();
            }
        }
    }

    open fun onError(data:VMData){
        showToastShort(data.msg);
    }

    open fun toLogin(){
        //AccountActivity.login(this,2);
    }

    open fun onAlbumSelect(requestCode : Int,pathList : MutableList<String>? = null){

    }

    open fun onCuttingResult(){

    }

    open fun onVideoSelect(requestCode : Int,path : String?){

    }

    override fun showLoading(loading: Loading) {
        when(loading.type){
            Loading.LoadingType.TYPE_DIALOG->{
                bssLoading?:let {
                    bssLoading = LoadingDialog(this);
                    if(allowedCancelWithNotEnding()){
                        bssLoading!!.setCancelable(true);
                        bssLoading!!.setCanceledOnTouchOutside(true);
                    }
                }
                if(bssLoading?.isShowing != true){
                    bssLoading?.show();
                }
            }
            Loading.LoadingType.TYPE_TOAST->{
                loadingToast(loading)
            }
        }
    }

    override fun dismissLoading(loading: Loading) {
        when(loading.type){
            Loading.LoadingType.TYPE_DIALOG->{
                bssLoading?.let {
                    if(it.isShowing){
                        it.dismiss();
                    }
                }
            }
            Loading.LoadingType.TYPE_TOAST->{
                disLoadingToast(loading)
            }
            Loading.LoadingType.TYPE_VIEW->{
                disLadingView(loading);
            }
        }
    }

    override fun addObserve() {
        super.addObserve()
    }

    open fun onViewClick(view : View){
        when(view.id){
            R.id.btn_submit ->{
                submitData();
            }
        }
    }

    fun addLineDivider(recyclerView: RecyclerView, lineResourceId : Int = R.drawable.line_default,
                       orientation : Int = DividerItemDecoration.VERTICAL ){
        val lineDivider = DividerItemDecoration(this, orientation)
        lineDivider.setDrawable(ContextCompat.getDrawable(this, lineResourceId)!!)
        recyclerView.addItemDecoration(lineDivider)
    }

    fun addOnScrollListener(recyclerView: RecyclerView,onLoadMoreListener: EastLessOnScrollListener.OnLoadMoreListener){
        val onLessOnScrollListener = EastLessOnScrollListener();
        onLessOnScrollListener.setOnScrollListener(onLoadMoreListener);
        recyclerView.addOnScrollListener(onLessOnScrollListener);
    }

    fun showToastShort(toast:String?){
        var _taost = toast?: let {
            getString(R.string.error_default);
        }.let {
            toast
        }
        ViewToast.show(this,_taost!!, Toast.LENGTH_SHORT)
    }

    fun showToastShort(resId:Int = R.string.error_default){
        ViewToast.show(this,getString(resId), Toast.LENGTH_SHORT)
    }

    fun showToastLong(toast:String? = getString(R.string.error_default)){
        var _taost = toast?: let {
            getString(R.string.error_default);
        }.let {
            toast
        }
        ViewToast.show(this,_taost!!, Toast.LENGTH_LONG)
    }

    fun showToastLong(resId:Int = R.string.error_default){
        ViewToast.show(this,getString(resId), Toast.LENGTH_LONG)
    }

    /**
     * 提交数据，在点击事件View为btn_submit时，触发点击事件将调用该方法
     * create by Administrator at 2022/6/18 14:17
     * @since 1.0.0
     * @author Administrator
     * @return
     *      void
     */
    open fun submitData(){

    }

    override fun onBackIconClickListener(menuImageView: ImageView) {
    }

    override fun onBackTextClickListener(backTextView: TextView) {
    }

    override fun onMenuIconClickListener(menuImageView: ImageView) {
    }

    override fun onMenuTextClickListener(menuTextView: TextView) {
    }

    override fun onSubTitleClickListener(subTitleView: TextView) {
    }

    override fun onTitleClickListener(titleTextView: TextView) {
    }
}

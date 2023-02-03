package com.star.light.common

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.starlight.dot.framework.commons.EastLessOnScrollListener
import com.starlight.dot.framework.commons.SDKActivity
import com.starlight.dot.framework.commons.SDKFragment
import com.starlight.dot.framework.entity.Loading
import com.starlight.dot.framework.entity.VMData
import com.starlight.dot.framework.utils.SLog
import com.starlight.dot.framework.widget.TitleLayout
import com.starlight.dot.framework.widget.ViewToast

abstract class AppFragment<D : ViewDataBinding,V : AppViewModel<*>> : SDKFragment<D, V>() {

    companion object{
        private const val TAG = "SL_AppFragment==>"
        const val REQEUST_CODE_CAMERA = 22;
        const val REQEUST_CODE_ALBUM = 23;
    }

    override fun onVmdataError(data: VMData) {
        when(data.error.erroCode){
            ConfigureHelper.LOGIN_ERROR -> toLogin()
            ConfigureHelper.LOGIN_TOKEN_ERROR -> toLogin()
            ConfigureHelper.AUTH_ERROR -> toLogin();
            ConfigureHelper.TOKEN_INVALID-> toLogin();
            else->onError(data)
        }
    }

    override fun showLoading(loading: Loading) {
        when(loading.type){
            Loading.LoadingType.TYPE_DIALOG->{
                val ac = activity;
                ac?.let {
                    (it as SDKActivity<*, *>).showLoading(loading);
                }
            }
            Loading.LoadingType.TYPE_TOAST->{
                loadingToast(loading)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG,"onActivityResult_requestCode=>${requestCode},resultCode=>${resultCode}");
    }

    override fun addObserve() {
        super.addObserve()
    }

    override fun dismissLoading(loading: Loading) {
        Log.d(TAG,"dismissLoading==>${loading.loadingFlag},type==>${loading.type}")
        super.dismissLoading(loading)
    }


    open fun onError(data: VMData){
        showToastShort(data.msg);
    }

    open fun toLogin(){
        //AccountActivity.login(requireContext(),2);
    }

    open fun onMenu(data:String?){

    }

    open fun menuDate() : String?{
        return null;
    }

    fun addLineDivider(recyclerView: RecyclerView, lineResourceId : Int = R.drawable.line_default,
                       orientation : Int = DividerItemDecoration.VERTICAL ){
        val lineDivider = DividerItemDecoration(requireContext(), orientation)
        lineDivider.setDrawable(ContextCompat.getDrawable(requireContext(), lineResourceId)!!)
        recyclerView.addItemDecoration(lineDivider)
    }


    fun addLineVerticalDivider(recyclerView: RecyclerView,color : Int,height : Int){
        val gd = GradientDrawable();
        gd.shape = GradientDrawable.LINE;
        gd.setSize(recyclerView.layoutParams.width,height);
        gd.setColor(color)
        val lineDivider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        lineDivider.setDrawable(gd);
        recyclerView.addItemDecoration(lineDivider);
    }

    fun addOnScrollListener(recyclerView: RecyclerView, onLoadMoreListener: EastLessOnScrollListener.OnLoadMoreListener){
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
        activity?.let { ViewToast.show(it,_taost!!, Toast.LENGTH_SHORT) }
    }

    fun showToastShort(resId:Int = R.string.error_default){
        activity?.let { ViewToast.show(it,getString(resId), Toast.LENGTH_SHORT) }
    }

    fun showToastLong(toast:String? = getString(R.string.error_default)){
        var _taost = toast?: let {
            getString(R.string.error_default);
        }.let {
            toast
        }
        activity?.let { ViewToast.show(it,_taost!!, Toast.LENGTH_LONG) }
    }

    fun showToastLong(resId:Int = R.string.error_default){
        activity?.let { ViewToast.show(it,getString(resId), Toast.LENGTH_LONG) }
    }


    open fun refreshFromActivity(jsonData:String? = null){

    }


    /**
     * 获取定义得资源颜色
     * create by Administrator at 2021/4/9 13:45
     * @author Administrator
     * @since 1.0.0
     * @param colorResId
     *      资源id
     * @return
     *      colors.xml中定义的颜色值对应的颜色
     */
    fun getColor(colorResId:Int): Int {
        return ContextCompat.getColor(requireContext(),colorResId);
    }

    open fun onViewClick(view : View){
        when(view.id){
            R.id.btn_submit ->{
                submitData();
            }
        }
    }

    open fun onBackTextClick(backTextView: TextView){
        SLog.d(TAG,"onBackTextClick==>");
    }

    open fun onBackIconClick(menuImageView: ImageView){
        SLog.d(TAG,"onBackIconClick==>");
    }

    open fun onTitleClick(titleTextView: TextView){
        SLog.d(TAG,"onTitleClick==>");
    }

    open fun onSubTitleClick(subTitleView: TextView){
        SLog.d(TAG,"onSubTitleClick==>");
    }

    open fun onMenuTextClick(menuTextView: TextView){
        SLog.d(TAG,"onMenuTextClick==>");
    }

    open fun onMenuIconClick(menuImageView: ImageView){
        SLog.d(TAG,"onMenuIconClick==>");
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
    open fun onAlbumSelect(requestCode : Int,pathList : MutableList<String>? = null){

    }

    /**
     * 请求跳转裁剪页面返回
     * create by Eastevil at 2022/9/14 10:31
     * @author Eastevil
     * @return
     *      void
     */
    open fun onCuttingResult(){

    }


    open fun onVideoSelect(requestCode : Int,videoData : String?){

    }

    /**
     * 由历史人物列表点击历史人物携带数据返回调用该方法
     * create by Administrator at 2022/7/23 15:22
     * @author Administrator
     * @since 1.0.0
     * @see com.star.light.history.commons.AppContainerActivity.onActivityResult
     * @param requestCode
     *      请求标识
     * @param personerJson
     *      选择的历史人物json格式数据
     * @return
     *      void
     */
    open fun onPersonSelect(requestCode : Int,personerJson : String?){

    }

    fun clearArgument(key : String){
        arguments?.remove(key);
    }

    fun clearArguments(){
        val keys = arguments?.keySet();
        keys?.forEach {
            arguments?.remove(it);
        }
    }

    /**
     * 获取当前容器Activity的标题布局View
     * create by Eastevil at 2022/7/19 15:00
     * @author Eastevil
     * @return
     *      容器Activity的标题布局View,如果当前所在的activity不为AppContainerActivity对象，则返回null
     */
    fun getContainerTitleLayout(): TitleLayout? {
        val ac = getAppActivity();
        if(ac is AppContainerActivity){
            return ac.dataBinding.slTitleLayout;
        }
        return null;
    }
}

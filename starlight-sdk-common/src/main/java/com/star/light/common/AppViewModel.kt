package com.star.light.common

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.star.light.common.entity.AppData
import com.star.light.brand.entity.Empty
import com.starlight.dot.framework.entity.Loading
import com.starlight.dot.framework.commons.SDKViewModel
import com.starlight.dot.framework.entity.VMData
import com.starlight.dot.framework.utils.isMainThread
import com.starlight.dot.framework.utils.mainThread
import com.starlight.dot.framework.entity.Error;
import com.star.light.common.entity.AppData.Companion.ERROR_CODE_DEFAULT
import com.star.light.common.entity.AppData.Companion.REQUEST_CODE_DEFAULT

abstract class AppViewModel<D : AppData>(application: Application) : SDKViewModel<D>(application) {
    companion object{
        private const val TAG = "LYNFREE_AppViewModel==>";
    }

    val emptyData = MutableLiveData<Empty>();

    val loginFlag = MutableLiveData<Boolean>();

    /**
     * 当前登录账号
     */
    //val account = MutableLiveData<Account>();

    override fun initModel() {
        super.initModel()
    }

    override fun showLoading(loadingType: Loading.LoadingType) {
        super.showLoading(loadingType)
    }

    override fun onResume() {
        super.onResume()
        /*loginFlag.value = AccountHelper.instance.isLogin();
        if(isLogin()){
            account.value = AccountHelper.instance.account();
        }*/
    }

    fun showLoadingView(){
        showLoading(Loading.LoadingType.TYPE_VIEW);
    }

    fun dismissViewLoading(){
        Log.d(TAG,"dismissViewLoading==>");
        var loading = this.loading.value;
        if(loading != null && loading.type == Loading.LoadingType.TYPE_VIEW){
            Log.d(TAG,"loading is not null and it's type_view,dismiss it");
            dismissLoading();
        }else{
            if(loading == null){
                Log.d(TAG,"loading is null,create a new type_view loading");
                loading = Loading("", Loading.LoadingType.TYPE_VIEW);
                loading!!.loadingFlag = false;
            }else{
                Log.d(TAG,"loading is not null,update type to type_view");
                loading.loadingFlag = false;
                loading.type = Loading.LoadingType.TYPE_VIEW;
            }
            if(isMainThread()){
                this.loading.value = loading;
            }else{
                mainThread {
                    this.loading.value = loading;
                }
            }

        }
    }

    fun dismissDialogLoading(){
        Log.d(TAG,"dismissLoading==>");
        var loading = this.loading.value;
        loading?.let {
            Log.d(TAG,"dismissLoading==>loading is not null");
            loading.loadingFlag = false;
            loading.type = Loading.LoadingType.TYPE_DIALOG;
            if(isMainThread()){
                Log.d(TAG,"dismissLoading==>update loading in mainthread");
                this.loading.value = loading;
            }else{
                mainThread {
                    Log.d(TAG,"dismissLoading==>update loading in mainthread");
                    this.loading.value = loading;
                }
            }
        }?:let {
            Log.d(TAG,"dismissLoading==>loading is null,now create a loading");
            val l = Loading();
            l.loadingFlag = false;
            l.type = Loading.LoadingType.TYPE_DIALOG
            Log.d(TAG,"dismissLoading==>make loading dismiss");
            if(isMainThread()){
                this.loading.value = l;
            }else{
                mainThread {
                    this.loading.value = l;
                }
            }
        }
    }

    override fun dismissLoading() {
        Log.d(TAG,"dismissLoading==>");
        var loading = this.loading.value;
        loading?.let {
            Log.d(TAG,"dismissLoading==>loading is not null");
            loading.loadingFlag = false;
            if(isMainThread()){
                Log.d(TAG,"dismissLoading==>update loading in mainthread");
                this.loading.value = loading;
            }else{
                mainThread {
                    Log.d(TAG,"dismissLoading==>update loading in mainthread");
                    this.loading.value = loading;
                }
            }
        }?:let {
            Log.d(TAG,"dismissLoading==>loading is null,now create a loading");
            val l = Loading();
            l.loadingFlag = false;
            l.type = Loading.LoadingType.TYPE_DIALOG
            Log.d(TAG,"dismissLoading==>make loading dismiss");
            if(isMainThread()){
                this.loading.value = l;
            }else{
                mainThread {
                    this.loading.value = l;
                }
            }
        }
    }

    open fun isLogin(): Boolean {
        //return AccountHelper.instance.isLogin();
        return false;
    }

    fun errorString(code:Int, msg:String? = getString(R.string.query_error), requestCode:Int){
        super.error(code,msg?:getString(R.string.query_error),requestCode)
    }

    fun toast(msg:String?,requestCode: Int= REQUEST_CODE_DEFAULT){
        val error = Error(ERROR_CODE_DEFAULT,msg?:getString(R.string.query_error));
        val data = vmData.value!!;
        data.error = error;
        data.code = VMData.Code.CODE_SHOW_MSG;
        data.msg = msg;
        data.requestCode = requestCode;
        if(isMainThread()){
            vmData.value = data;
        }else {
            mainThread {
                vmData.value = data;
            }
        }
    }

    fun toast(msgRes:Int,requestCode: Int= REQUEST_CODE_DEFAULT){
        val msg = getString(msgRes);
        val error = Error(ERROR_CODE_DEFAULT,msg);
        val data = vmData.value!!;
        data.error = error;
        data.code = VMData.Code.CODE_SHOW_MSG;
        data.msg = msg;
        data.requestCode = requestCode;
        if(isMainThread()){
            vmData.value = data;
        }else {
            mainThread {
                vmData.value = data;
            }
        }
    }

    fun getString(resId:Int,vararg args:String): String {
        return getApplication<Application>().getString(resId,*args);
    }

    fun getColor(id:Int): Int {
        return getApplication<Application>().getColor(id);
    }

    fun queryErrorText():String{
        return getString(R.string.query_error);
    }

    fun networkErrorText():String{
        return getString(R.string.error_network);
    }

    fun showError(code:Int = ERROR_CODE_DEFAULT, msg:String?=getString(R.string.query_error),
                  requestCode:Int = REQUEST_CODE_DEFAULT
    ){
        error(code, msg ?: getString(R.string.query_error), requestCode);
    }

    fun getStringArray(id : Int): MutableList<String> {
        return getApplication<Application>().resources.getStringArray(id).toMutableList();
    }
}

package com.star.light.common.network.model

import com.star.light.common.ConfigureHelper
import com.star.light.common.entity.request.SendmsgReqeust
import com.star.light.common.network.service.AppService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class AppModel private constructor(){

    companion object {
        private const val TAG = "SL_AppModel==>";
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { AppModel() }

        private val appService : AppService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ConfigureHelper.instance.appService }
    }

    /**
     * app预请求接口
     * create by Eastevil at 2022/9/23 16:40
     * @author Eastevil
     * @since 1.0.0
     * @see com.star.light.brand.model.splash.SplashViewModel.appBeforehand
     * @return
     *      token信息
     */
    suspend fun appBeforehand() = withContext(Dispatchers.IO){
        return@withContext  appService.appBeforehand();
    }

    /**
     * 发送短信验证码
     * create by Eastevil at 2022/10/21 13:35
     * @author Eastevil
     * @since 1.0.0
     * @param mobile
     *      手机号码
     * @param type
     *      验证码类型,1:账号登录,2:注册
     * @return
     *      发送结果
     */
    suspend fun sendMessage(mobile : String,type : Int) = withContext(Dispatchers.IO){
        val request = SendmsgReqeust();
        request.type = type;
        request.mobile = mobile;
        return@withContext appService.sendMessage(request);
    }

    /**
     * 查询应用底部导航栏
     * create by Eastevil at 2022/10/12 15:54
     * @author Eastevil
     * @param appId
     *      应用id
     * @return
     *      应用底部导航栏
     */
    suspend fun tabList(appId: Long) = withContext(Dispatchers.IO){
        return@withContext  appService.tabList(appId);
    }

    /**
     * 获取应用首页配置
     * create by Administrator at 2022/10/13 22:35
     * @author Administrator
     * @param appId
     *      应用ID
     * @return
     *      首页配置
     */
    suspend fun appHomeConfig(appId : Long) = withContext(Dispatchers.IO){
        return@withContext  appService.appHomeConfig(appId);
    }

    /**
     * 文件上传
     * create by Eastevil at 2022/9/14 14:18
     * @author Eastevil
     * @param file
     *      上传的文件对象
     * @param type
     *      [com.star.light.common.network.UploadFileType]中定义的类型
     * @return
     *      上传结果
     */
    suspend fun uploadFile(file : File,type : Int) = withContext(Dispatchers.IO){
        val body: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val part = MultipartBody.Part.createFormData("file", file.name, body);
        return@withContext appService.uploadFile(part,type);
    }

    suspend fun uploadFile(fileList : MutableList<File>,type : Int) = withContext(Dispatchers.IO){
        val paratList = mutableListOf<MultipartBody.Part>();
        fileList.forEach {
            val body: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), it)
            val part = MultipartBody.Part.createFormData("files", it.name, body);
            paratList.add(part);
        }
        return@withContext appService.uploadFile(paratList,type);
    }

    /**
     * 获取欢迎页活动
     * create by Eastevil at 2022/9/21 11:10
     * @author Eastevil
     * @return
     *      欢迎页活动
     */
    suspend fun splashEvent(appId : Long) = withContext(Dispatchers.IO){
        return@withContext appService.splashEvent(appId);
    }

    /**
     * 查询轮播图列表
     * create by Eastevil at 2022/10/14 13:08
     * @author Eastevil
     * @param appId
     *      应用id
     * @param type
     *      类型，1-欢迎页活动,2-首页banner,3-历史tab栏banner
     * @return
     *      轮播图列表
     */
    suspend fun bannerList(appId: Long,type : Int) = withContext(Dispatchers.IO){
        return@withContext appService.bannerlist(appId,type);
    }

}

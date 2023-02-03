package com.star.light.common.network.service

import com.star.light.common.entity.AppHomeConfig
import com.star.light.common.entity.AppTabEntity
import com.star.light.common.entity.EastEvilEvent
import com.star.light.common.entity.reponse.AppBeforehandReponse
import com.star.light.common.entity.reponse.EventPageReponse
import com.star.light.common.entity.reponse.UploadFileListReponse
import com.star.light.common.entity.reponse.UploadFileReponse
import com.star.light.common.entity.request.SendmsgReqeust
import com.starlight.dot.framework.network.entity.Result
import okhttp3.MultipartBody
import retrofit2.http.*
import kotlin.collections.HashMap

interface AppService{

    /**
     * app预请求接口
     * create by Eastevil at 2022/9/23 16:38
     * @author Eastevil
     * @param
     * @return
     */
    @GET("api/app/beforehand")
    fun appBeforehand() : Result<AppBeforehandReponse>;

    /**
     * 发送短信验证码
     * create by Eastevil at 2022/10/21 13:28
     * @author Eastevil
     * @param
     * @return
     */
    @POST("api/app/sendmsg")
    fun sendMessage(@Body request : SendmsgReqeust) : Result<Any?>;

    /**
     * 查询应用底部导航栏
     * create by Eastevil at 2022/10/12 15:54
     * @author Eastevil
     * @param appId
     *      应用id
     * @return
     *      应用底部导航栏列表
     */
    @GET("api/app/tablist/{appId}")
    fun tabList(@Path("appId") appId : Long) : Result<MutableList<AppTabEntity>>

    /**
     * 获取首页配置
     * create by Administrator at 2022/10/13 22:34
     * @author Administrator
     * @param appId
     *      应用ID
     * @return
     *      首页配置
     */
    @GET("api/home/homeconfig/{appId}")
    fun appHomeConfig(@Path("appId") appId : Long) : Result<AppHomeConfig>;

    /**
     * 文件上传
     * create by Eastevil at 2022/9/14 14:01
     * @author Eastevil
     * @param file
     *      文件
     * @return
     *      文件上传后的url地址实体类
     */
    @Multipart
    @POST("api/oss/upload/{type}")
    fun uploadFile(@Part file : MultipartBody.Part,@Path("type") type : Int) : Result<UploadFileReponse>;

    @Multipart
    @POST("api/oss/upload/filelist/{type}")
    fun uploadFile(@Part() filePart : List<MultipartBody.Part>,@Path("type") type : Int) : Result<UploadFileListReponse>;

    /**
     * 查询应用欢迎页活动数据
     * create by Eastevil at 2022/9/21 10:54
     * @author Eastevil
     * @param appId
     *      应用id
     * @return
     *      欢迎页数据
     */
    @GET("api/event/splash/{appId}")
    fun splashEvent(@Path("appId") appId : Long) : Result<EastEvilEvent>;

    /**
     * 查询首页轮播图
     * create by Eastevil at 2022/10/14 13:06
     * @author Eastevil
     * @param appId
     *      应用id
     * @param type
     *      类型，1-欢迎页活动,2-首页banner,3-历史tab栏banner
     * @return
     *      首页轮播图列表
     */
    @GET("api/event/bannerlist/{appId}/{type}")
    fun bannerlist(@Path("appId") appId: Long,@Path("type") type : Int) : Result<MutableList<EastEvilEvent>>;

}

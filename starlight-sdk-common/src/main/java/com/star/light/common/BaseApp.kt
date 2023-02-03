package com.star.light.common

import android.app.Application

/**
 * 项目中所有应用项目的application都需要继承此类
 * create by Eastevil at 2022/10/10 13:38
 * @author Eastevil
 */
abstract class BaseApp : Application(),RetrofitServiceCallBack {

    override fun onCreate() {
        super.onCreate()
        instance = this
        ConfigureHelper.instance.init(appBaseUrl(),this);
    }

    override fun getRetrofitService(): HashMap<String, Class<*>>? {
        return registerRetrofitService();
    }

    /**
     * 注册Retrofit接口
     * create by Administrator at 2022/10/11 22:57
     * @author Administrator
     * @return
     *      自定义的Retrofit接口，Map的key为Retrofit接口的唯一标识，Value为Retrofit接口
     */
    abstract fun registerRetrofitService() : HashMap<String, Class<*>>?;

    /**
     * 应用接口请求地址
     * create by Administrator at 2022/10/11 23:26
     * @author Administrator
     * @return
     *      应用接口请求地址
     */
    abstract fun appBaseUrl() : String;

    companion object{
        private lateinit var instance: BaseApp
        val app
            get() = instance
    }
}

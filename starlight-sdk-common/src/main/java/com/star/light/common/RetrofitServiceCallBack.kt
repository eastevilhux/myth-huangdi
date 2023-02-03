package com.star.light.common

/**
 * 初始化需要使用的参数通过该回调接口获取
 * create by Eastevil at 2022/10/10 14:11
 * @author Eastevil
 */
interface RetrofitServiceCallBack {

    /**
     * 返回所有需要Retrofit初始化的service集合
     * create by Eastevil at 2022/10/10 14:10
     * @author Eastevil
     * @param
     * @return
     */
    fun getRetrofitService() : HashMap<String,Class<*>>?;
}

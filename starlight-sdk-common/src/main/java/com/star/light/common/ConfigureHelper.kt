package com.star.light.common

import com.east.network.utils.AESUtil
import com.star.light.common.network.interceptor.RequestInterceptor
import com.star.light.common.network.service.AppService
import com.starlight.dot.framework.network.retrofit.RetrofitConfigure
import com.starlight.dot.framework.network.HttpConfig
import com.starlight.dot.framework.network.NetworkHelper
import com.starlight.dot.framework.network.datasource.DataHelper
import com.starlight.dot.framework.network.interceptor.HttpInterceptor
import com.starlight.dot.framework.utils.SLog
import java.util.*
import kotlin.collections.HashMap
import com.starlight.dot.framework.network.SDKDataSecurity
import com.starlight.dot.framework.utils.DeviceExt
import com.starlight.dot.framework.utils.RSAUtil
import com.star.light.common.BuildConfig
import java.lang.NullPointerException

class ConfigureHelper private constructor(){

    companion object{
        private const val TAG = "SL_ConfigureHelper==>";

        private const val TOKEN_KEY_SP = "star_light_sp_token_key";

        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ConfigureHelper() }

        private lateinit var baseUrl: String;

        /**
         * 登录验证码url地址
         */
        private val CAPTCHA_URL: String
            get() =
                when (BuildConfig.VERSION_TYPE) {
                    BuildConfig.VERSION_DEV -> "http://192.168.0.103:9088/starlight/bms/captcha?uuid=${DeviceExt.deviceId(BaseApp.app)}"
                    BuildConfig.VERSION_SIT -> "http://192.168.0.182:9088/starlight/bms/captcha?uuid=${DeviceExt.deviceId(BaseApp.app)}"
                    BuildConfig.VERSION_UAT -> "http://192.168.0.126:9088/starlight/bms/captcha?uuid=${DeviceExt.deviceId(BaseApp.app)}"
                    BuildConfig.VERSION_PERSONAL-> "http://192.168.0.103:9088/starlight/bms/captcha?uuid=${DeviceExt.deviceId(BaseApp.app)}"
                    BuildConfig.VERSION_RELEASE -> "http://192.168.0.129:9088/starlight/bms/captcha?uuid=${DeviceExt.deviceId(BaseApp.app)}"
                    else -> throw IllegalStateException("version type error")
                }

        private const val SERVICE_KEY_APP = "star_light_evil_app_app_service";

        const val LOGIN_ERROR = 700;

        /**
         * 为授权
         */
        const val AUTH_ERROR = 401;

        const val LOGIN_TOKEN_ERROR = 701;

        const val TOKEN_INVALID = 10021;

        /**
         * 前后端交互的token
         */
        private var appToken : String? = null;

        private var isInit = false;

        private var keyType : Int = 0;
        private var dataSecret : String? = null;

        const val KEY_TYPE_RSA = 1;
        const val KEY_TYPE_AES = 2;
    }

    private lateinit var map : HashMap<String, Any>;

    /**
     * 初始化系统框架
     * create by Eastevil at 2022/10/10 14:11
     * @author Eastevil
     * @param callBack
     *      [RetrofitServiceCallBack]
     * @return
     *      void
     */
    fun init(baseUrl : String,callBack : RetrofitServiceCallBack? = null){
        SLog.setDebug(true)
        ConfigureHelper.baseUrl = baseUrl;
        initNetwork();
        map = initRetrofit(callBack?.getRetrofitService())
        DataHelper.instance.setSdkDataSecurity(sdkDataSecurity);
        isInit = true;
    }

    /**
     * 获取登录验证码显示地址
     * create by Eastevil at 2022/9/13 14:13
     * @author Eastevil
     * @return
     *      登录验证码显示地址
     */
    fun captchaUrl(): String {
        return CAPTCHA_URL;
    }

    private fun initNetwork(){
        SLog.setDebug(true);
        var builder = HttpConfig.Builder(baseUrl)
            .charset("UTF-8")
            .loginErrorCode(LOGIN_ERROR)
            .successCode(Constants.HTTP_SUCCESS_CODE)
            .emptyErrorCode(Constants.CODE_EMPTY)
            .networkErrorCode(404)
            .needURLDecoder(true)
            .needBase64(false)
            .encryptParams(false)
            .addInterceptor(RequestInterceptor())
        NetworkHelper.init(builder);
        NetworkHelper.instance().addHttpHealder(header);
    }


    private fun initRetrofit(serviceMap : HashMap<String,Class<*>>?): java.util.HashMap<String, Any> = synchronized(this){
        synchronized(this){
            var m = HashMap<String, Class<*>>();
            m.put(SERVICE_KEY_APP, AppService::class.java)
            serviceMap?.let {
                m.putAll(serviceMap);
            }
            //使用默认配置
            val map = RetrofitConfigure.createByUrl(baseUrl,m);
            return map;
        }
    }

    val appService : AppService by lazy(mode=LazyThreadSafetyMode.SYNCHRONIZED) {
        if(!isInit){
            throw IllegalAccessException("star light sdk it not init,please init it befor");
        }
        map[SERVICE_KEY_APP] as AppService;
    }

    fun getService(serviceKey : String): Any {
        val clazz = map.get(serviceKey);
        if(clazz == null){
            return NullPointerException("get service is null");
        }
        return clazz;
    }

    /**
     * 注册Retrofit接口
     * create by Eastevil at 2022/10/18 13:19
     * @author Eastevil
     * @param key
     *      接口对应的key，通过该key后续获取对应的service
     * @param clazz
     *      接口对象
     */
    fun registerService(key : String,clazz : Class<*>){
        if(!isInit){
            throw IllegalAccessException("star light sdk it not init,please init it befor");
        }
        val s = RetrofitConfigure.registerService(baseUrl,clazz);
        map.put(key,s);
    }

    /**
     * 注册Retrofit接口
     * create by Eastevil at 2022/10/18 13:19
     * @author Eastevil
     * @param map
     *      接口对应的key，通过该key后续获取对应的service
     */
    fun registerService(map : Map<String,Class<*>>){
        if(!isInit){
            throw IllegalAccessException("star light sdk it not init,please init it befor");
        }
        this.map.putAll(map);
    }

    fun token(): String? {
        if(appToken != null && appToken!!.isNotEmpty()){
            return appToken;
        }else{
            //再从缓存中获取一次，
            appToken = SharedPreferencesUtil.getString(BaseApp.app, TOKEN_KEY_SP);
            if(appToken != null && appToken!!.isNotEmpty()){
                return appToken;
            }
        }
        return null;
    }

    /**
     * 重置token
     * create by Administrator at 2021/4/23 13:23
     * @author Administrator
     * @param token
     *      重置后的token
     * @return
     *      void
     */
    fun resetToken(token : String){
        if(token.isNotEmpty()){
            appToken = token;
            SharedPreferencesUtil.putData(BaseApp.app, TOKEN_KEY_SP, appToken);
        }
    }

    fun setSecret(keyType : Int,secret : String){
        Companion.keyType = keyType;
        dataSecret = secret;
    }

    fun dismissToken(){
        appToken = null;
        SharedPreferencesUtil.putData(BaseApp.app, TOKEN_KEY_SP,"");
    }

    /**
     * 对请求数据进行加密处理
     * create by Eastevil at 2022/6/15 17:28
     * @author Eastevil
     * @param data
     *      需要加密的数据
     * @return
     *      加密后的数据
     */
    fun encryptData(data : String) : String{
        return sdkDataSecurity.dataEncrypt(data)?:"";
    }

    var header = object : HttpInterceptor.HttpHeader{
        override fun getHealder(): TreeMap<String, String> {
            var map = TreeMap<String,String>();
            map.put("token",token()?:"");
            return map;
        }
    }

    private val sdkDataSecurity = object : SDKDataSecurity{

        override fun dataDecrypt(data: String?): String? {
            if(dataSecret == null){
                throw IllegalAccessException("dataSecret is null");
            }
            if(keyType == KEY_TYPE_AES){
                return AESUtil.aesDecrypt(data, dataSecret!!);
            }else if(keyType == KEY_TYPE_RSA){
                return RSAUtil.decryptByPublicKey(data, dataSecret);
            }else{
                throw IllegalAccessException("unknow data secret type");
            }
        }

        /**
         * 数据加密处理
         * create by Administrator at 2022/10/15 23:39
         * @author Administrator
         * @param data
         *      需要加密的数据
         * @return
         *      加密后的数据
         */
        override fun dataEncrypt(data: String?): String? {
            if(data == null){
                return data;
            }
            if(dataSecret == null){
                throw IllegalAccessException("dataSecret is null");
            }
            val s = if(keyType == KEY_TYPE_AES){
                AESUtil.aesEncrypt(data, dataSecret!!);
            }else if(keyType == KEY_TYPE_RSA){
                RSAUtil.encryptByPublicKey(data, dataSecret);
            }else{
                throw IllegalAccessException("unknow data secret type");
            }
            if(s == null){
                throw IllegalAccessException("data encrypt error");
            }
            return s;
        }
    }
}

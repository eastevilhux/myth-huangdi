package com.star.light.common.network.interceptor

import com.star.light.common.ConfigureHelper
import com.starlight.dot.framework.utils.SLog
import com.starlight.dot.framework.utils.toJSON
import okhttp3.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URLEncoder
import java.nio.charset.Charset

class RequestInterceptor : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val method = request.method().toLowerCase().trim()
        //字符集
        var charset: Charset? = Charset.forName("UTF-8")
        //返回url
        val url = request.url()
        //http://192.168.0.108:8080/interface/xxx   //@get @delete 时候需要拼接在请求地址后面  ?userName=xiaoming&userPassword=12345
        val scheme = url.scheme() //协议http
        val host = url.host() //host地址,192.168.0.108
        val port = url.port() //端口8080
        val path = url.encodedPath() //  /interface/login
        val originalPath = "$scheme://$host:$port$path"
        if(!METHOD_GET.equals(method) &&  !METHOD_DELETE.equals(method)){
            //获取body中的参数
            val requestBody = request.body()
            requestBody?.let {requestBody->
                //判断类型
                val contentType: MediaType? = requestBody.contentType()
                if(contentType != null){
                    charset = contentType.charset(charset);
                    /*如果是二进制上传  则不进行加密*/
                    if (contentType.type().toLowerCase().equals("multipart")) {
                        return chain.proceed(request);
                    }
                }
                // 获取请求的数据
                var buffer = okio.Buffer();
                requestBody.writeTo(buffer);
                val inputStream = buffer.inputStream();
                val inputStreamReader = InputStreamReader(inputStream);
                val reader = BufferedReader(inputStreamReader);
                val results = StringBuilder()
                while (true) {
                    val line = reader.readLine() ?: break
                    results.append(line)
                }
                reader.close();
                inputStreamReader.close();
                inputStream.close();
                var requestData = results.toString();
                SLog.d(TAG,"reqeust_data plaintext=>${requestData}");
                //对请求数据进行加密处理
                requestData = ConfigureHelper.instance.encryptData(requestData);
                SLog.d(TAG,"reqeust_data ciphertext=>${requestData}");
                requestData = URLEncoder.encode(requestData,"UTF-8");
                SLog.d(TAG,"reqeust_data urlencoder=>${requestData}");
                if(requestData != null && !requestData.isNullOrEmpty()){
                    val requestMap = HashMap<String,String>();
                    requestMap.put("data",requestData);
                    val newRequestData = requestMap.toJSON();
                    SLog.d(TAG,"create new request body,data is=>${newRequestData}");
                    val newRequestBody = RequestBody
                        .create(MediaType.parse("application/json"),newRequestData);
                    //构建新的requestBuilder
                    val newRequestBuilder = request.newBuilder()
                    newRequestBuilder.post(newRequestBody);
                    return chain.proceed(newRequestBuilder.build());
                }
                return chain.proceed(request);
            }
        }
        //模式使用原有的request
        return chain.proceed(request);
    }

    companion object {
        private const val TAG = "SHAPP_RequestInterceptor==>"
        const val METHOD_GET = "get"
        const val METHOD_DELETE = "delete";
        const val METHOD_POST = "post"

        /**
         * 服务端接口定义需要接受的参数key为data
         */
        const val SERVICE_DATA_KEY = "data";
        const val HEADER_KEY_USER_AGENT = "User-Agent"
    }

}

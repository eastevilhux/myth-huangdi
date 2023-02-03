package com.star.light.common.entity.request

class SendmsgReqeust{

    /**
     * 验证码类型,1:账号登录,2:注册
     */
    var type : Int = 1;

    /**
     * 手机号码
     */
    var mobile : String? = null;

}

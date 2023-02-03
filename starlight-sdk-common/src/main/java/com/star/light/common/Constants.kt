package com.star.light.common

import android.Manifest
import android.os.Build
import com.starlight.dot.framework.network.datasource.DataHelper

class Constants {

    companion object{
        /**
         * 网络请求成功标识
         */
        const val HTTP_SUCCESS_CODE = 0
        const val CODE_ERROR = 500;
        const val TOKEN_ERROR = 401;
        const val CODE_ACCOUNT_ERROR = 400;
        const val CODE_SIGN_ERROR = 700;
        const val CODE_APPID_ERROR = 701;
        const val CODE_EMPTY = 704;
        const val CODE_ERROR_NOTENOUGH = 800;
    }


    /**
     * 定义厂家名称
     */
    object BrandName{
        const val HUAWEI = "HUAWEI";
        const val XIAOMI = "XIAOMI";
        const val VIVO = "VIVO";
        const val OPPO = "OPPO";
        const val SAMSUNG = "SAMSUNG";
        const val MEIZU = "MEIZU";
        const val LENOVO = "LENOVO";
        const val ZTE = "ZTE";
        const val M360 = "360";
        const val ONEPLUS = "ONEPLUS";
        const val UNKNOW = "FUCK_UNKNOW";
    }

    object AlubmPermission{
        /**
         * 相册所需权限
         */
        val PERMISS_ALUBM = arrayOf(
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        /**
         * 相机需要权限
         */
        val PERMISS_CAMERA = arrayOf(
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
        );

    }
}

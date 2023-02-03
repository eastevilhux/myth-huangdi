package com.star.light.common.entity

import com.starlight.dot.framework.entity.VMData

abstract class AppData : VMData() {
    companion object{

        const val HEALTH_APP_DATA_KEY = "health_app_api_data_key";

        const val REQUEST_CODE_DEFAULT = 0x00;
        const val ERROR_CODE_DEFAULT = -1;
        const val SUCCESS_CODE_DEFAULT = 88;
    }
}

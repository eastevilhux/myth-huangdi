package com.star.light.common.entity

import android.net.Uri
import java.io.File

class IconUri {
    var sourceUri : Uri? = null;
    var cuttingUri : Uri? = null;
    var cuttingFile : File? = null;

    var tag : Int = 0;

    var widht : Int = 0;
    var height : Int = 0;

    /**
     * 比例
     */
    var scale : Int = Scale.SCALE_SOUCE;

    fun deleteCuttingFile(){
        cuttingFile?.delete()
    }

    object Scale{
        /**
         * 原始比例
         */
        const val SCALE_SOUCE = 0;

        /**
         * 1：1
         */
        const val SCALE_1_1 = 1;

        /**
         * 4:3
         */
        const val SCALE_4_3 = 43;

        /**
         * 3：2
         */
        const val SCALE_3_2 = 32;

        /**
         * 16：9
         */
        const val SCALE_16_9 = 169;
    }
}

package com.star.light.common.ext

import android.app.Activity
import androidx.fragment.app.Fragment
import com.star.light.common.entity.IconUri
import com.star.light.common.model.image.ImageData
import com.yalantis.ucrop.UCrop

fun Activity.uCropCutting(iconUri : IconUri){
    var x : Float = 0f;
    var f : Float = 0f;
    when(iconUri.scale){
        IconUri.Scale.SCALE_SOUCE->{
            x = 0f;
            f = 0f;
        }
        IconUri.Scale.SCALE_16_9->{
            x = 16f;
            f = 9f;
        }
        IconUri.Scale.SCALE_1_1->{
            x = 1f;
            f = 1f;
        }
        IconUri.Scale.SCALE_3_2->{
            x = 3f;
            f = 2f;
        }
        IconUri.Scale.SCALE_4_3->{
            x = 4f;
            f = 3f;
        }
    }
    if(iconUri.sourceUri != null && iconUri.cuttingUri != null){
        if(iconUri.scale == IconUri.Scale.SCALE_SOUCE){
            UCrop.of(iconUri.sourceUri!!, iconUri.cuttingUri!!)
                .withMaxResultSize(iconUri.widht, iconUri.height)
                .start(this, ImageData.REQUEST_STAR_CUTTING);
        }else{
            UCrop.of(iconUri.sourceUri!!, iconUri.cuttingUri!!)
                .withAspectRatio(x, f)
                .withMaxResultSize(iconUri.widht, iconUri.height)
                .start(this,ImageData.REQUEST_STAR_CUTTING);
        }
    }
}

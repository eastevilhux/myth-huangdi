package com.star.light.common.model.image

import android.app.Application
import com.star.light.common.AppViewModel
import com.star.light.common.model.image.ImageData

class ImageViewModel(application: Application) : AppViewModel<ImageData>(application) {

    override fun initData(): ImageData {
        return ImageData();
    }

}

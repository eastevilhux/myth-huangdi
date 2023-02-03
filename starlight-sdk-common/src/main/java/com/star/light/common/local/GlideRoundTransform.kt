package com.star.light.common.local

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.CenterCrop

/**
 * 圆角为10的加载方式
 */
class GlideRoundTransform : CenterCrop {
    private var radius = 10f

    constructor(){

    }

    constructor(radius : Float){
        this.radius = radius;
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap? {
        //glide4.0+

        //glide4.0+
        val transform = super.transform(pool, toTransform, outWidth, outHeight)
        return roundCrop(pool, transform);
    }

    private fun roundCrop(
        pool: BitmapPool,
        source: Bitmap?
    ): Bitmap? {
        if (source == null) return null
        var result: Bitmap? = pool[source.width, source.height, Bitmap.Config.ARGB_8888]
        if (result == null) {
            result = Bitmap.createBitmap(
                source.width,
                source.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(result)
            val paint = Paint()
            paint.setShader(
                BitmapShader(
                    source,
                    Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP
                )
            )
            paint.setAntiAlias(true)
            val rectF = RectF(0f, 0f, source.width.toFloat(), source.height.toFloat())
            canvas.drawRoundRect(rectF, radius, radius, paint)
        }
        return result
    }

}

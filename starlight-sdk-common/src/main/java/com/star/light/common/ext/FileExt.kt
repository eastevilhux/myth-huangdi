package com.star.light.common.ext

import android.content.ContentResolver
import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.net.toFile
import java.io.*
import java.math.BigDecimal
import kotlin.random.Random

fun uriToFileQ(context: Context, uri: Uri): File? =
    if (uri.scheme == ContentResolver.SCHEME_FILE)
        uri.toFile()
    else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        //把文件保存到沙盒
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                var ois : InputStream? = null;
                try {
                    ois = context.contentResolver.openInputStream(uri)
                }catch (e : Exception){
                    e.printStackTrace();
                    return null;
                }
                val displayName =
                    it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                ois?.let {
                    File(
                        context.externalCacheDir!!.absolutePath,
                        "${Random.nextInt(0, 9999)}$displayName"
                    ).apply {
                        val fos = FileOutputStream(this)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            android.os.FileUtils.copy(ois, fos)
                        }
                        fos.close()
                        it.close()
                    }
                }
            } else null

        }
    } else null


private fun rotateImage(file : File,orientationDegree : Int): Bitmap? {
    //方便判断，角度都转换为正值
    var degree = orientationDegree
    if( degree < 0){
        degree = 360 + orientationDegree;
    }
    var bm = BitmapFactory.decodeFile(file.path);
    var srcW: Int = bm.getWidth()
    var srcH: Int = bm.getHeight()

    val m = Matrix()
    m.setRotate(degree.toFloat(), srcW.toFloat() / 2, srcH.toFloat() / 2)
    var targetX: Float
    var targetY: Float

    var destH = srcH
    var destW = srcW

    //根据角度计算偏移量，原理不明
    if (degree == 90 ) {
        targetX = srcH.toFloat();
        targetY = 0F;
        destH = srcW;
        destW = srcH;
    } else if( degree == 270){
        targetX = 0F;
        targetY = srcW.toFloat();
        destH = srcW;
        destW = srcH;
    }else if(degree == 180){
        targetX = srcW.toFloat();
        targetY = srcH.toFloat();
    }else {
        return bm;
    }
    val values = FloatArray(9)
    m.getValues(values)
    val x1 = values[Matrix.MTRANS_X]
    val y1 = values[Matrix.MTRANS_Y]
    m.postTranslate(targetX - x1, targetY - y1);

    //注意destW 与 destH 不同角度会有不同
    val bm1 = Bitmap.createBitmap(destW, destH, Bitmap.Config.ARGB_8888)
    val paint = Paint()
    val canvas = Canvas(bm1)
    canvas.drawBitmap(bm, m, paint)
    return bm1
}


private fun changeImageLocate(filepath: String, bitmap: Bitmap): Bitmap? {
    //根据图片的filepath获取到一个ExifInterface的对象
    var bitmap = bitmap
    var degree = 0
    try {
        val exif = ExifInterface(filepath)
        if (exif != null) {
            // 读取图片中相机方向信息
            val ori = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            Log.e("degree========ori====", ori.toString() + "")
            degree = when (ori) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
            Log.e("degree============", degree.toString() + "")
            if (degree != 0) {
                Log.e("degree============", "degree != 0")
                // 旋转图片
                val m = Matrix()
                //m.setScale(0.5f,0.5f);
                m.postRotate(degree.toFloat())
                bitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    m,
                    true
                )
                return bitmap
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return bitmap
}

/**
 * 保存Bitmap为本地文件，如果传入的保存路径存在，则会删除
 * create by Administrator at 2022/3/28 0:19
 * @author Administrator
 * @param savePath
 *      保存路径
 * @return
 *      保存后的文件
 */
private fun Bitmap.bitmapToFile(savePath : String): File? {
    val file = File(savePath);
    if(file.exists()){
        file.delete();
    }
    var fos : FileOutputStream? = null;
    var bos : BufferedOutputStream? = null;
    try {
        fos = FileOutputStream(file);
        bos = BufferedOutputStream(fos);
        this.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        return file;
    } catch (e : java.lang.Exception){
        e.printStackTrace();
    } finally {
        fos?.let {
            it.close()
        }
        bos?.let {
            it.close();
        }
    }
    return null;
}

/**
 * 压缩图片,先对图片进大小压缩，然后再进行质量压缩
 * create by Eastevil at 2022/7/20 10:44
 * @author Eastevil
 * @since 1.0.0
 * @param dirPath
 *      压缩后的图片保存的文件地址
 * @param srcPath
 *      源文件所在地址
 * @return
 *      压缩后的文件
 */
fun File.compressSize(width : Float = 1080f,dirPath : String,srcPath : String): File? {
    val newOpts = BitmapFactory.Options()
    // 开始读入图片，此时把options.inJustDecodeBounds 设回true了，只读取图片的大小，不分配内存
    newOpts.inJustDecodeBounds = true
    var bitmap = BitmapFactory.decodeFile(srcPath, newOpts) // 此时返回bm为空
    newOpts.inJustDecodeBounds = false
    val w = newOpts.outWidth
    val ww = width
    // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
    var be = 1 // be=1表示不缩放
    if (w > ww) { // 如果宽度大的话根据宽度固定大小缩放
        be = (newOpts.outWidth / ww).toInt()
    }
    if (be <= 0) be = 1
    newOpts.inSampleSize = be // 设置缩放比例
    // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
    bitmap = BitmapFactory.decodeFile(srcPath, newOpts)
    var b1: Bitmap? = null
    if (bitmap != null) {
        //检测并旋转图片
        b1 = changeImageLocate(srcPath, bitmap)
    }
    return if (b1 != null) {
        return compressImage(dirPath, b1, srcPath)
    } else null
}


/**
 * 质量压缩图片
 * create by Eastevil at 2022/3/16 10:17
 * @author Eastevil
 * @param
 * @return
 */
@Throws(IOException::class)
private fun compressImage(dirPath: String, bitmap: Bitmap, oldPath: String): File? {
    val baos = ByteArrayOutputStream()
    // 质量压缩
    var isCompress = false
    if (oldPath.endsWith(".jpg") || oldPath.endsWith(".jpeg")) {
        isCompress = true
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    } else if (oldPath.endsWith(".png")) {
        isCompress = true
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
    } else {
        isCompress = false
    }
    if (isCompress) {
        var options = 100
        var length = baos.toByteArray().size.toLong()
        length = length / 1024
        if (length > 200) {
            val legBig = BigDecimal(length)
            length = legBig.multiply(BigDecimal(0.25)).toLong();
            while (baos.toByteArray().size / 1024 > length) {
                options -= 5 // 每次都减少5
                baos.reset() // 重置baos即清空baos
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos)
            }
        }
    }
    val dirFile = File(dirPath);
    if (!dirFile.exists()) {
        dirFile.mkdirs()
    }
    var child = System.currentTimeMillis().toString()
    child = if (oldPath.endsWith(".jpg")) {
        "$child.jpg"
    } else if (oldPath.endsWith(".png")) {
        "$child.png"
    } else if (oldPath.endsWith(".jpeg")) {
        "$child.jpeg"
    } else {
        "$child.jpg"
    }
    val file = File(dirFile.path, child)
    val fos = FileOutputStream(file)
    fos.write(baos.toByteArray())
    fos.flush()
    fos.close()
    try {
        fos.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    recycleBitmap(bitmap)
    return file
}


private fun recycleBitmap(vararg bitmaps: Bitmap?) {
    if (bitmaps == null) {
        return
    }
    for (bm in bitmaps) {
        if (null != bm && !bm.isRecycled) {
            bm.recycle()
        }
    }
}

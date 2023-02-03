package com.star.light.common.model.image

import com.star.light.common.entity.AppData

class ImageData : AppData() {

    companion object{
        const val FRAGMENT_IMAGE_KEY = "star_album_image_fragment_key";
        const val DATA_IMAGE_KEY = "star_album_image_data_key";
        const val ALBUM_TITLE_COLOR_KEY = "star_album_title_color_key";
        const val REQEUST_ALBUM_CODE = 800;
        const val REQUEST_CAMERA_CODE = 801;

        /**
         * 请求相册code标识
         */
        const val REQUEST_STAR_ALBUM = 300;

        /**
         * 请求拍摄视频标识
         */
        const val REQUEST_VIDEO_CODE = 301;

        /**
         * 请求裁剪标识
         */
        const val REQUEST_STAR_CUTTING = 400;

        /**
         * 相册返回code标识
         */
        const val RESULT_CODE_STAR_ALBUM = 400;

        /**
         * 相册返回code标识
         */
        const val RESULT_CODE_VIDEO = 402;

        /**
         * 裁剪返回code标识
         */
        const val RESULT_CODE_STAR_CUTTING = 401;


        const val REQEUST_ALBUM_PERMISSION_CODE = 100;
        const val REQUEST_CAMERA_PERMISSION_CODE = 101;

        const val STAR_IMAGE_PATH_LIST = "star_image_pathslist_key";

        /**
         * intent传递视频路径的key
         */
        const val VIDEO_DATA = "star_video_path_key";
    }

    object FragmentKey{
        const val FRAGMENT_IMAGE_LIST = "star_a_i_f_list_key";
    }
}

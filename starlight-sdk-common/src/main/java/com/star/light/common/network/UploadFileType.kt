package com.star.light.common.network

/**
 * 文件上传类型
 */
class UploadFileType private constructor(){

    companion object{
        /**
         * 视频文件
         */
        const val TYPE_VIDEO = 10000;

        /**
         * 视频封面
         */
        const val TYPE_VIDEO_CONVER = 10001;

        /**
         * 动态show time类型
         */
        const val TYPE_MOMENT_SHOWTIME = 10002;

        /**
         * 文章图片
         */
        const val TYPE_ARTICLE = 10003;

        /**
         * 文章封面
         */
        const val TYPE_ARTICLE_COVER = 10004;

        /**
         * 个人图像
         */
        const val TYPE_ACCOUNT_ICON = 10005;

        /**
         * 我的页面用户头部背景
         */
        const val TYPE_ACCOUNT_BGHEADER = 10006;

        /**
         * 动态瞬间图片
         */
        const val TYPE_MOMENT_ICON= 10007;

        /**
         * 话题图片
         */
        const val TYPE_TOPIC_ICON = 10008;

        /**
         * 用户我的栏头部背景图片
         */
        const val TYPE_MINE_HEADER = 10009;

        /**
         * 动态顶部背景图
         */
        const val TYPE_MOMENT_HEADER = 10010;
    }
}

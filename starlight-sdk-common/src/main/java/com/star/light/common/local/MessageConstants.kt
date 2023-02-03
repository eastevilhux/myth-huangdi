package com.star.light.common.local

class MessageConstants {

    companion object{
        /**
         * 用户成功建立连接后发送消息告诉服务端头部标识
         */
        const val MSG_HEADER_USERNAMECONN = 10000;

        /**
         * 关闭连接头部消息标识
         */
        const val MSG_HEADER_EXIT = 10001;

        /**
         * 普通消息头部标识
         */
        const val MSG_HEADER_MESSAGE = 10002;

        /**
         * 修改用户名头部标识
         */
        const val MSG_HEADER_UPDATEUSERNAME = 10003;

        /**
         * 打分头部标识
         */
        const val MSG_HEADER_SCORE = 10004;

        /**
         * 得分结果头部标识
         */
        const val MSG_HEADER_SCORE_RESULT = 10005;

        /**
         * 通知得分结果头部标识
         */
        const val MSG_HEARER_NOTIFY_SCORE = 10006;

        /**
         * 断开连接头部标识
         */
        const val MSG_HEADER_CLOSE = 10007;

        /**
         * 开启打分头部标识
         */
        const val MSG_HEADER_OPENSCORE = 10008;

        /**
         * 关闭打分头部标识
         */
        const val MSG_HEADER_CLOSESCORE = 10009;

        /**
         * 用户成功建立连接后发送的消息尾部标识
         */
        const val MSG_ENDING_USERNAMECONN = 100000;


        /**
         * 关闭连接尾部标识
         */
        const val MSG_ENDING_EXIT = 100001;

        /**
         * 普通消息尾部标识
         */
        const val MSG_ENDING_MESSAGE = 100002;

        /**
         * 修改用户名尾部标识
         */
        const val MSG_ENDING_UPDATEUSERNAME = 100003;

        /**
         * 打分尾部标识
         */
        const val MSG_ENDING_SCORE = 100004;

        /**
         * 打分结果尾部标识
         */
        const val MSG_ENDING_SCORE_RESULT = 100005;

        /**
         * 通知得分尾部标识
         */
        const val MSG_ENDING_NOTIFY_SCORE = 100006;

        /**
         * 断开连接尾部标识
         */
        const val MSG_ENDING_CLOSE = 100007;

        /**
         * 开启打分尾部标识
         */
        const val MSG_ENDING_OPENSCORE = 100008;

        /**
         * 关闭打分尾部标识
         */
        const val MSG_ENDING_CLOSESCORE = 100009;
    }
}

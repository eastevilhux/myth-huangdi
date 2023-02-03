package com.star.light.common.entity

import com.star.starlight.ui.view.entity.adapter.BaseUniversal
import com.star.starlight.ui.view.entity.adapter.ItemType

class EastEvilEvent : BaseUniversal() {
    /**
     * 活动链接地址
     */
    var linkUrl : String? = null;

    var startTime : Long = 0;
    var endTime : Long = 0;

    /**
     * 类型，1:欢迎页活动,2:首页banner
     */
    var type : Int = 0;

    /**
     * 当前列表item展示的布局类型，通过该方法的返回值来确定列表item的展示布局
     * [com.starlight.dot.framework.entity.adapter.ItemType]来设定
     * create by Eastevil at 2022/8/19 14:42
     * @author Eastevil
     * @return
     *      item布局类型
     */
    override fun itemViewType(): Int {
        return ItemType.UNIVERSAL_CUSTOM;
    }

    /**
     * 当[itemViewType]方法返回为[com.starlight.dot.framework.entity.adapter.ItemType.UNIVERSAL_CUSTOM]时，
     * 表示当前的item为自定义item类型，需要通过该方法获取item展示的资源id，如果此时该方法返回0，则adapter会抛出异常
     * create by Eastevil at 2022/10/10 13:31
     * @author Eastevil
     * @return
     *      自定义item布局资源id
     */
    override fun customItemResourceId(): Int {
        return 0;
    }
}

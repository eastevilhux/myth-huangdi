package com.star.light.common.entity

import com.star.starlight.ui.view.entity.adapter.BaseUniversal
import com.star.starlight.ui.view.entity.adapter.ItemType

open class AppTabEntity : BaseUniversal() {

    var name : String? = null;

    var selIcon : String? = null;
    var norIcon : String? = null;
    var selColor : String = "#000000";
    var norColor : String = "#8a8a8a";
    var iconWidth : Int = 0;
    var iconHeight : Int = 0;

    /**
     * 底部导航对应的fragment类型
     */
    var fragmentType : Int = 0;

    /**
     * 底部导航对应的fragment code标识
     */
    var fragmentCode : String? = null;
    /**
     * 当前列表item展示的布局类型，通过该方法的返回值来确定列表item的展示布局
     * [com.starlight.dot.framework.entity.adapter.ItemType]来设定
     * create by Eastevil at 2022/8/19 14:42
     * @author Eastevil
     * @return
     *      item布局类型
     */
    override fun itemViewType(): Int {
        return ItemType.APP_TAB;
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

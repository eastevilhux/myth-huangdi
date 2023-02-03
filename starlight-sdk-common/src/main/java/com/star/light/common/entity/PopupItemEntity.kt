package com.star.light.common.entity

import java.io.File

class PopupItemEntity{
    var id : Long;
    var iconResId : Int = 0;
    var iconUrl : String? = null;
    var showText : String? = null;

    /**
     * 0:不展示
     * 1:本地资源类型
     * 2:url地址类型
     * 3:文件类型
     */
    var iconType : Int = 1;
    var iconFile : File? = null;
    var tag : Any? = null;

    private constructor(builder : Builder){
        this.id = builder.id;
        this.iconResId = builder.iconResId;
        this.iconUrl = builder.iconUrl;
        this.showText = builder.showText;
        this.iconFile = builder.iconFile;
        this.iconType = builder.iconType;
        this.tag = builder.tag;
    }

    fun setTAG(tag : Any){
        this.tag = tag;
    }

    fun getTAG(): Any? {
        return tag;
    }


    class Builder{
        internal var id : Long;
        internal var iconResId : Int = 0;
        internal var iconUrl : String? = null;
        internal var showText : String? = null;
        /**
         * 0:不展示
         * 1:本地资源类型
         * 2:url地址类型
         * 3:文件类型
         */
        var iconType : Int = 0;
        var iconFile : File? = null;
        var tag : Any ? = null;

        constructor(id : Long){
            this.id = id;
        }

        fun icon(iconResId : Int): Builder {
            this.iconResId = iconResId;
            this.iconType = 1;
            return this;
        }

        fun icon(iconUrl : String): Builder {
            this.iconUrl = iconUrl;
            this.iconType = 2;
            return this;
        }

        fun icon(iconFile : File): Builder {
            this.iconFile = iconFile;
            this.iconType = 3;
            return this;
        }

        fun showText(showText : String): Builder {
            this.showText = showText;
            return this;
        }

        fun objectTag(tag : Any): Builder {
            this.tag = tag;
            return this;
        }

        fun builder(): PopupItemEntity {
            return PopupItemEntity(this);
        }
    }
}

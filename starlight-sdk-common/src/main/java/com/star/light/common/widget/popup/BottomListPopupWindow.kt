package com.star.light.common.widget.popup

import android.content.Context
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.GridLayoutManager
import com.star.light.common.R
import com.star.light.common.adapter.BottomPopupItemAdapter
import com.star.light.common.databinding.PopupBottomGridBinding
import com.star.light.common.entity.PopupItemEntity
import com.starlight.dot.framework.widget.BasePopupWindow
import com.starlight.dot.framework.widget.PopupBaseBuilder
import java.io.File

class BottomListPopupWindow : BasePopupWindow<PopupBottomGridBinding, BottomListPopupWindow.Builder>{
    private lateinit var adapter : BottomPopupItemAdapter;
    private lateinit var menuText : ObservableField<String>;
    private lateinit var haveMenu : ObservableField<Boolean>;
    private var onPopupItemEntityClick : ((entity : PopupItemEntity)->Unit)? = null;

    private constructor(builder : Builder) : super(builder) {
        dataBinding.rvPopupList.layoutManager = GridLayoutManager(dataBinding.root.context,4);
        adapter = BottomPopupItemAdapter(builder.getContext(),builder.dataList);
        this.onPopupItemEntityClick = builder.onPopupItemEntityClick;
        adapter.onPopupItemEntityClick {
            onPopupItemEntityClick?.invoke(it);
        }
        dataBinding.adapter = adapter;

        dataBinding.titleText = titleText;
        haveMenu = ObservableField(builder.haveMenu);
        menuText = ObservableField(builder.menuText?:"");
        dataBinding.haveMenu = haveMenu;
        dataBinding.haveSubmit = haveSubmit;
        dataBinding.submitText = submitText;
        dataBinding.menuText = menuText
    }

    override fun getResourceId(): Int {
        return R.layout.popup_bottom_grid;
    }

    fun notifyList(dataList: MutableList<PopupItemEntity>){
        adapter.dataList = dataList;
        adapter.notifyDataSetChanged();
    }

    companion object{

        fun buildEntity(id : Long,iconResId : Int,name : String): PopupItemEntity {
            val e = PopupItemEntity.Builder(id)
                .icon(iconResId)
                .showText(name)
                .builder();
            return e;
        }

        fun buildEntity(id : Long,iconUrl : String,name : String): PopupItemEntity {
            val e = PopupItemEntity.Builder(id)
                .icon(iconUrl)
                .showText(name)
                .builder();
            return e;
        }

        fun buildEntity(id : Long, iconFile : File, name : String): PopupItemEntity {
            val e = PopupItemEntity.Builder(id)
                .icon(iconFile)
                .showText(name)
                .builder();
            return e;
        }
    }

    class Builder(context: Context) : PopupBaseBuilder<BottomListPopupWindow>(context) {
        internal var dataList : MutableList<PopupItemEntity>? = null;
        internal var haveMenu : Boolean = false;
        internal var menuText : String? = null;
        internal var onPopupItemEntityClick : ((entity : PopupItemEntity)->Unit)? = null;

        fun dataList(dataList : MutableList<PopupItemEntity>): Builder {
            this.dataList = dataList;
            return this;
        }

        fun haveMenu(haveMenu : Boolean): Builder {
            this.haveMenu = haveMenu;
            return this;
        }

        fun menuText(menuText : String): Builder {
            this.menuText = menuText;
            return this;
        }

        fun onPopupItemEntityClick(onPopupItemEntityClick : ((entity : PopupItemEntity)->Unit)): Builder {
            this.onPopupItemEntityClick = onPopupItemEntityClick;
            return this;
        }

        override fun builder(): BottomListPopupWindow {
            return BottomListPopupWindow(this);
        }

    }

}

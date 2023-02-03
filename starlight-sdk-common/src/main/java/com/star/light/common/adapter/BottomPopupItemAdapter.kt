package com.star.light.common.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.star.light.common.R
import com.star.light.common.BR
import com.star.light.common.entity.PopupItemEntity
import com.star.light.common.presenter.PopupItemEntityPresenter
import com.star.light.common.presenter.impl.PopupItemEntityPresenterImpl
import com.starlight.dot.framework.commons.EastAdapter

class BottomPopupItemAdapter(context: Context?, dataList: MutableList<PopupItemEntity>?) :
    EastAdapter<PopupItemEntity>(context, dataList) {

    private var onPopupItemEntityClick : ((entity : PopupItemEntity)->Unit)? = null;

    override fun getViewItemType(position: Int): Int {
        return R.layout.recycler_item_bottomgrid
    }

    override fun bindItem(map: MutableMap<Int, Int>?) {
        map?.put(R.layout.recycler_item_bottomgrid,BR.popupEntity);
    }

    fun onPopupItemEntityClick(onPopupItemEntityClick : ((entity : PopupItemEntity)->Unit)){
        this.onPopupItemEntityClick = onPopupItemEntityClick;
    }

    override fun setBean(dataBinding: ViewDataBinding?, varid: Int, position: Int) {
        super.setBean(dataBinding, varid, position)
        dataBinding?.setVariable(BR.presenter,presenter);
    }

    private val presenter = object : PopupItemEntityPresenterImpl() {

        override fun onItemSelectListener(entity: PopupItemEntity) {
            super.onItemSelectListener(entity)
            onPopupItemEntityClick?.invoke(entity);
        }
    }
}

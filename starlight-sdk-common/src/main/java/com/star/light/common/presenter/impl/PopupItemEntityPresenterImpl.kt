package com.star.light.common.presenter.impl

import com.star.light.common.entity.PopupItemEntity
import com.star.light.common.presenter.PopupItemEntityPresenter

abstract class PopupItemEntityPresenterImpl : PopupItemEntityPresenter{
    /**
     * 列表Item点击事件监听
     * create by Eastevil at 2022/5/10 15:46
     * @author Eastevil
     * @param entity
     *      点击的item对象
     * @return
     *      void
     */
    override fun onItemClickListener(entity: PopupItemEntity, position: Int) {
    }

    /**
     * 列表Item选择事件监听
     * create by Eastevil at 2022/5/10 15:46
     * @author Eastevil
     * @param entity
     *      选择的item对象
     * @return
     *      void
     */
    override fun onItemSelectListener(entity: PopupItemEntity) {
    }

    /**
     * 列表Item菜单按钮点击事件监听
     * create by Eastevil at 2022/5/10 15:46
     * @author Eastevil
     * @param entity
     *      点击的Item对象
     * @return
     *      void
     */
    override fun onItemMenuClickListener(entity: PopupItemEntity) {
    }

    /**
     * 列表Item编辑事件监听
     * create by Eastevil at 2022/5/10 15:46
     * @author Eastevil
     * @param entity
     *      需要编辑的item对象
     * @return
     *      void
     */
    override fun onEditListener(entity: PopupItemEntity) {
    }
}

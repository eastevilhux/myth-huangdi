package com.star.light.common.adapter

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.star.light.common.R
import com.star.light.common.BR
import com.star.light.common.entity.AlbumImage
import com.star.light.common.presenter.impl.AlbumImagePresenterImpl
import com.starlight.dot.framework.commons.EastAdapter
import com.starlight.dot.framework.utils.SLog
import com.starlight.dot.framework.utils.dip2px
import com.starlight.dot.framework.utils.getScreenSize
import kotlin.math.max

class AlbumImageAdapter : EastAdapter<AlbumImage>{
    companion object{
        private const val TAG = "SL_Album_ImageAdapter==>";
    }

    private var params : ConstraintLayout.LayoutParams? = null;
    private var onCamera : (()->Unit)? = null;
    private var onAlbumImageSelect : ((AlbumImage)->Unit)? = null;
    private var selectList : MutableList<AlbumImage>? = null;
    private var onSelectFinish : ((selectList : MutableList<String>)->Unit)? = null;
    private var onVideoSelect : ((albumImage : AlbumImage) -> Unit)? = null;
    /**
     * 最大选择数量
     */
    private var maxSelect : Int = 1;

    private var selectFinish : Boolean = false;

    constructor(context: Context,list: MutableList<AlbumImage>? = null) : super(context,list) {
        val width = getScreenSize(context)[0];
        val size = (width - 3.dip2px())/4;
        params = ConstraintLayout.LayoutParams(size,size);
    }

    override fun bindItem(map: MutableMap<Int, Int>?) {
        map?.put(R.layout.recycler_item_albumimg,BR.albumImage);
    }

    override fun getViewItemType(p0: Int): Int {
        return R.layout.recycler_item_albumimg;
    }

    override fun setView(rootView: View?, viewType: Int, position: Int) {
        super.setView(rootView, viewType, position)
        rootView?.layoutParams = params;
    }

    override fun setBean(dataBinding: ViewDataBinding?, varid: Int, position: Int) {
        super.setBean(dataBinding, varid, position)
        dataBinding?.setVariable(BR.presenter,presenter);
        dataBinding?.setVariable(BR.position,position);
    }

    /**
     * 设置最大选择数量
     * create by Administrator at 2022/7/2 14:43
     * @author Administrator
     * @param maxNum
     *      最大选择数量
     * @return
     *      void
     */
    fun setMaxNum(maxNum : Int){
        this.maxSelect = maxNum;
    }


    fun onCamera(onCamera : (()->Unit)){
        this.onCamera = onCamera;
    }

    fun onAlbumImageSelect(onAlbumImageSelect : ((AlbumImage)->Unit)){
        this.onAlbumImageSelect = onAlbumImageSelect;
    }

    fun onSelectFinish(onSelectFinish : ((selectList : MutableList<String>)->Unit)){
        this.onSelectFinish = onSelectFinish;
    }

    fun onVideoSelect(onVideoSelect : ((albumImage : AlbumImage) -> Unit)){
        this.onVideoSelect = onVideoSelect;
    }

    private val presenter = object : AlbumImagePresenterImpl() {

        override fun onItemClickListener(entity: AlbumImage, position: Int) {
            super.onItemClickListener(entity, position)
            if(entity.type == 3){
                onVideoSelect?.invoke(entity);
            } else if(entity.type == 2){
                onCamera?.invoke();
            }else{
                if(selectList == null){
                    selectList = mutableListOf();
                }
                if(entity.select){
                    //设置为未选中状态
                    entity.select = false;
                    notifyItemChanged(position);
                    selectList!!.remove(entity);
                    if(selectList!!.size < maxSelect){
                        selectFinish = false;
                    }
                }else {
                    if(selectFinish){
                        //选择已完成，不在继续进行选择
                        return;
                    }
                    entity.select = true;
                    notifyItemChanged(position)
                    selectList!!.add(entity);
                    onAlbumImageSelect?.invoke(entity);
                    //添加之后判断是否已经达到最大选择数量
                    if(selectList!!.size >= maxSelect){
                        //已达到最大选择数量
                        selectFinish = true;
                        selectFinish();
                    }
                }
            }
        }
    }

    fun selectFinish(){
        SLog.d(TAG,"selectFinish");
        val pathList = mutableListOf<String>();
        selectList?.let {
            it.forEach {a->
                val path  = a.image?.path;
                path?.let {p->
                    pathList.add(p);
                }
            }
        }
        onSelectFinish?.invoke(pathList);
    }
}

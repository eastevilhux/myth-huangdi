package com.star.light.common.model.image

import android.graphics.Color
import com.star.light.common.model.image.list.ListFragment
import com.star.light.common.AppContainerActivity
import com.star.light.common.R
import com.starlight.dot.framework.commons.SDKFragment
import com.starlight.dot.framework.widget.TitleLayout

class ImageActivity : AppContainerActivity<ImageViewModel>() {
    override fun createFragment(): HashMap<String, SDKFragment<*, *>> {
        val map = HashMap<String,SDKFragment<*,*>>();
        map.put(ImageData.FragmentKey.FRAGMENT_IMAGE_LIST, ListFragment.newInstance());
        return map;
    }

    override fun getDefaultShowtKey(): String {
        return ImageData.FRAGMENT_IMAGE_KEY;
    }

    override fun getDefaultDataKey(): String? {
        return ImageData.DATA_IMAGE_KEY;
    }

    override fun getVMClass(): Class<ImageViewModel> {
        return ImageViewModel::class.java;
    }

    override fun initTitle(key: String, titleLayout: TitleLayout) {
        super.initTitle(key, titleLayout)
        val titleColor = intent.getIntExtra(ImageData.ALBUM_TITLE_COLOR_KEY,getColor(R.color.slColorMainTheme));
        titleLayout.setBackgroundColor(titleColor);
        titleLayout.setMenuType(TitleLayout.MenuType.TYPE_TEXT);
        titleLayout.setMenuText(getString(R.string.menu_select_img))
        titleLayout.setMenuTextColor(Color.WHITE);
    }
}

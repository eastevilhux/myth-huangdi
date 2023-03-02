package com.east.evil.myth.huangdi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.star.starlight.ui.view.commons.RichAdapter
import com.star.starlight.ui.view.commons.RichBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = RichBuilder(this)
            .builder();

    }
}

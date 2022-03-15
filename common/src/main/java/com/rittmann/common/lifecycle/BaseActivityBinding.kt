package com.rittmann.common.lifecycle

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rittmann.common.R
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseBindingActivity<T : ViewDataBinding>(private val resId: Int) :
    DaggerAppCompatActivity() {
    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, resId)
        binding.lifecycleOwner = this

        binding.root.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.robbie_color_light_background_primary
            )
        )
    }
}
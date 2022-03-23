package com.rittmann.common.lifecycle

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rittmann.common.R
import com.rittmann.common.extensions.visible
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

    fun configureToolbar(title: String? = null, onClickFilter: (() -> Unit)? = null) {
        if (title != null)
            findViewById<TextView>(R.id.toolbar_title)?.text = title

        findViewById<View>(R.id.toolbar_filter)?.apply {
            if (onClickFilter != null) {
                visible()
                setOnClickListener {
                    onClickFilter.invoke()
                }
            }
        }
    }
}
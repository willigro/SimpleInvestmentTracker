package com.rittmann.common.lifecycle

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rittmann.common.R
import com.rittmann.common.extensions.visible
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseBindingActivity<T : ViewDataBinding>(private val resId: Int) :
    DaggerAppCompatActivity() {

    @VisibleForTesting
    lateinit var binding: T

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

    fun configureToolbar(
        title: String? = null,
        hasBack: Boolean = false,
        onClickFilter: (() -> Unit)? = null
    ) {
        if (title != null)
            findViewById<TextView>(R.id.toolbar_title)?.text = title

        if (hasBack)
            findViewById<View>(R.id.toolbar_back)?.apply {
                visible()
                setOnClickListener {
                    onBackPressed()
                }
            }

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
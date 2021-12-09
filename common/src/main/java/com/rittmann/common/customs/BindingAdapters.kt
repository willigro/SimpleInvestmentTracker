package com.rittmann.common.customs

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.rgames.meusgastos.ui.base.DataBindingBaseAdapter

object BindingAdapters {
    @BindingAdapter("app:isVisible")
    @JvmStatic
    fun isVisible(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

//    @BindingAdapter("app:items")
//    @JvmStatic
//    fun RecyclerView.setItems(items: List<Nothing>?) {
//        (adapter as? DataBindingBaseAdapter<*>)
//            ?.swapData(items ?: listOf())
//    }

    @BindingAdapter("app:customBackgroundColor")
    @JvmStatic
    fun setCustomBackgroundColor(view: View, color: Int) {
        view.setBackgroundColor(ContextCompat.getColor(view.context, color))
    }

    @BindingAdapter("app:customTextColor")
    @JvmStatic
    fun TextView.setCustomTextColor(color: Int) {
        setTextColor(ContextCompat.getColor(context, color))
    }

    @BindingAdapter("app:auxStyle")
    @JvmStatic
    fun TextView.setAuxStyle(style: Int) {
        TextViewCompat.setTextAppearance(this, style)
        invalidate()
    }
}
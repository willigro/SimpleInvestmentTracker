package com.rittmann.crypto.listmovements.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.rittmann.common.customs.views.createBottomSheet
import com.rittmann.crypto.R
import java.util.ArrayList

class BottomSheetFilters {

    private var tagControl: DisplayTagsControl? = null

    @SuppressLint("InflateParams")
    fun createBottomSheetFilter(
        context: Context,
        names: List<String>,
        tradeFilter: TradeFilter,
        onClickConfirm: () -> Unit
    ) {
        createBottomSheet(
            LayoutInflater.from(context)
                .inflate(R.layout.content_trade_filters, null, false)
        ).apply {
            val linear = findViewById<LinearLayoutCompat>(R.id.list_filter_item_by_name)

//            findViewById<TextView>(R.id.txt_title).text =
//                context.getString(R.string.trade_movement_list_filter_title)

            findViewById<TextView>(R.id.txt_msg).text =
                context.getString(R.string.trade_movement_list_filter_message)

            findViewById<View>(R.id.btn_confirm).setOnClickListener {
                tradeFilter.nameFilters.clear()
                tradeFilter.nameFilters.addAll(tradeFilter.tempNameFilters)
                onClickConfirm()
            }

            val tags = arrayListOf<Tag>().apply {
                names.forEachIndexed { index, name ->
                    val tag = Tag(index.toLong(), name)
                    if (tradeFilter.nameFilters.contains(name))
                        tag.itemSelected = true
                    add(tag)
                }
            }

            tradeFilter.tempNameFilters.clear()
            tradeFilter.tempNameFilters.addAll(tradeFilter.nameFilters)

            tagControl = DisplayTagsControl(
                context,
                linear,
                tags,
                changeColorOnSelect = true,
                controlOptions = true,
                onClickTag = { tag, selected, execute ->
                    tag?.apply {
                        itemSelected = selected

                        if (tradeFilter.tempNameFilters.contains(tag.name)) {
                            tradeFilter.tempNameFilters.remove(tag.name)
                            tagControl?.unselect(tag)
                        } else {
                            tradeFilter.tempNameFilters.add(name)
                            tagControl?.select(tag)
                        }
                    }
                }
            )
        }
    }
}
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

object BottomSheetFilters {

    @SuppressLint("InflateParams")
    fun createBottomSheetFilter(context: Context, names: List<String>, tradeFilter: TradeFilter, onClickConfirm: () -> Unit){
        createBottomSheet(
            LayoutInflater.from(context)
                .inflate(R.layout.content_trade_filters, null, false)
        ).apply {
            val linear = findViewById<LinearLayoutCompat>(R.id.list_filter_item_by_name)

            findViewById<TextView>(R.id.txt_title).text =
                context.getString(R.string.trade_movement_list_filter_title)

            findViewById<TextView>(R.id.txt_msg).text =
                context.getString(R.string.trade_movement_list_filter_message)

            findViewById<View>(R.id.btn_confirm).setOnClickListener {
                tradeFilter.nameFilters.clear()
                tradeFilter.nameFilters.addAll(tradeFilter.tempNameFilters)
                onClickConfirm()
            }

            for (name in names) {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.adapter_trade_filter_names, null, false)
                val textView = view.findViewById<TextView>(R.id.txt_dialog_trade_filter_name)
                textView.text = name
                linear.addView(view)

                if (tradeFilter.nameFilters.contains(name))
                    textView.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.robbie_ripple_color
                        )
                    )
                else
                    textView.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.black
                        )
                    )

                tradeFilter.tempNameFilters.clear()
                tradeFilter.tempNameFilters.addAll(tradeFilter.nameFilters)

                view.setOnClickListener {
                    if (tradeFilter.tempNameFilters.contains(name)) {
                        tradeFilter.tempNameFilters.remove(name)
                        textView.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.black
                            )
                        )
                    } else {
                        tradeFilter.tempNameFilters.add(name)
                        textView.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.robbie_ripple_color
                            )
                        )
                    }
                }
            }
        }
    }
}
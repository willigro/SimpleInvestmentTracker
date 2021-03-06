package com.rittmann.crypto.listmovements.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rittmann.crypto.R
import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.basic.CurrencyType
import com.rittmann.common.extensions.gone
import com.rittmann.common.extensions.visible
import com.rittmann.common.utils.DateUtil
import com.rittmann.common.utils.FormatDecimalController

private class DiffCallback : DiffUtil.ItemCallback<TradeMovement>() {

    override fun areItemsTheSame(oldItem: TradeMovement, newItem: TradeMovement) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TradeMovement, newItem: TradeMovement) =
        oldItem == newItem
}

class RecyclerAdapterCryptoMovement(
    list: List<TradeMovement>,
    private val navigation: ListCryptoMovementsNavigation,
    private val onDeleteClicked: (TradeMovement) -> Unit
) : ListAdapter<TradeMovement, RecyclerAdapterCryptoMovement.CryptoMovementViewHolder>(DiffCallback()) {

    private lateinit var mContext: Context

    init {
        submitList(list.toMutableList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoMovementViewHolder {
        if (::mContext.isInitialized.not()) {
            mContext = parent.context
        }
        return if (viewType == CONTENT) CryptoMovementViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_crypto_movements, parent, false)
        ) else CryptoMovementViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_crypto_movements_date, parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].useThisDate) DATE_AND_CONTENT else CONTENT
    }

    override fun onBindViewHolder(holder: CryptoMovementViewHolder, position: Int) {
        currentList[holder.adapterPosition].also { cryptoMovement ->
            holder.apply {

                val totalValueColor =
                    if (cryptoMovement.type.value == CryptoOperationType.SELL.value
                        || cryptoMovement.type.value == CryptoOperationType.WITHDRAW.value
                    )
                        ContextCompat.getColor(mContext, R.color.accent_primary)
                    else
                        ContextCompat.getColor(mContext, R.color.accent_secondary)

                titleDate?.text = DateUtil.simpleDateFormat(cryptoMovement.date)

                deleteMovement?.setOnClickListener {
                    onDeleteClicked(cryptoMovement)
                }

                name?.text = cryptoMovement.name

                if (cryptoMovement.type.value == CryptoOperationType.DEPOSIT.value || cryptoMovement.type.value == CryptoOperationType.WITHDRAW.value) {
                    coinName?.text = cryptoMovement.cryptoCoin
                    coinNameContainer?.visible()
                } else
                    coinNameContainer?.gone()

                boughtAmount?.text =
                    FormatDecimalController.format(
                        cryptoMovement.operatedAmount,
                        CurrencyType.DECIMAL
                    )
                currentValue?.text =
                    FormatDecimalController.format(
                        cryptoMovement.currentValue,
                        cryptoMovement.currentValueCurrency
                    )
                totalValue?.apply {
                    text =
                        FormatDecimalController.format(
                            cryptoMovement.totalValue,
                            cryptoMovement.totalValueCurrency
                        )
                    setTextColor(totalValueColor)
                }
                concreteTotalValue?.apply {
                    text =
                        FormatDecimalController.format(
                            cryptoMovement.concreteTotalValue,
                            cryptoMovement.totalValueCurrency
                        )
                    setTextColor(totalValueColor)
                }
                tax?.text =
                    FormatDecimalController.format(
                        cryptoMovement.tax,
                        cryptoMovement.taxCurrency
                    )
                layout?.setOnClickListener {
                    navigation.goToUpdate(cryptoMovement)
                }
            }
        }
    }

    override fun getItemCount(): Int = currentList.size

    override fun submitList(list: MutableList<TradeMovement>?) {
        var lastDate = ""
        list?.forEach { cash ->
            cash.useThisDate = false
            val date = DateUtil.simpleDateFormat(cash.date)
            if (lastDate != date) {

                lastDate = date

                cash.useThisDate = true
            }
        }
        super.submitList(list)
    }

    class CryptoMovementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout: View? = itemView.findViewById(R.id.adapter_crypto_movement_layout)
        val deleteMovement: View? = itemView.findViewById(R.id.adapter_crypto_delete_icon)
        val name: TextView? = itemView.findViewById(R.id.adapter_crypto_movement_name)
        val coinName: TextView? = itemView.findViewById(R.id.adapter_crypto_movement_coin_name)
        val coinNameContainer: View? =
            itemView.findViewById(R.id.adapter_crypto_movement_coin_container)

        //        val date: TextView? = itemView.findViewById(R.id.adapter_crypto_movement_date)
//        val type: TextView? = itemView.findViewById(R.id.adapter_crypto_movement_type)
        val boughtAmount: TextView? =
            itemView.findViewById(R.id.adapter_crypto_movement_bought_amount)
        val currentValue: TextView? =
            itemView.findViewById(R.id.adapter_crypto_movement_current_value)
        val totalValue: TextView? = itemView.findViewById(R.id.adapter_crypto_movement_total_value)
        val concreteTotalValue: TextView? =
            itemView.findViewById(R.id.adapter_crypto_movement_concrete_total_value)
        val tax: TextView? = itemView.findViewById(R.id.adapter_crypto_movement_tax)
        val titleDate: TextView? = itemView.findViewById(R.id.adapter_crypto_movement_title_date)
    }

    companion object {
        private const val DATE_AND_CONTENT = 1
        private const val CONTENT = 0
    }
}
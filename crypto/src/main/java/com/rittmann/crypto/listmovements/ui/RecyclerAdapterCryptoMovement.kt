package com.rittmann.crypto.listmovements.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rittmann.crypto.R
import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.basic.CurrencyType
import com.rittmann.common.utils.FormatDecimalController

class RecyclerAdapterCryptoMovement(
    list: List<CryptoMovement>,
    private val navigation: ListCryptoMovementsNavigation,
    private val onDeleteClicked: (CryptoMovement) -> Unit
) : RecyclerView.Adapter<RecyclerAdapterCryptoMovement.CryptoMovementViewHolder>() {

    private var listMovements: List<CryptoMovement> = emptyList()
        set(value) {
            var lastDate = ""
            value.forEach { cash ->
                if (lastDate != cash.date) {

                    lastDate = cash.date

                    cash.useThisDate = true
                }
            }
            field = value
        }

    init {
        listMovements = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoMovementViewHolder {
        return if (viewType == CONTENT) CryptoMovementViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_crypto_movements, parent, false)
        ) else CryptoMovementViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_crypto_movements_date, parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return if (listMovements[position].useThisDate) DATE_AND_CONTENT else CONTENT
    }

    override fun onBindViewHolder(holder: CryptoMovementViewHolder, position: Int) {
        listMovements[holder.adapterPosition].also { cryptoMovement ->
            holder.apply {

                titleDate?.text = cryptoMovement.date

                deleteMovement?.setOnClickListener {
                    onDeleteClicked(cryptoMovement)
                }

                name?.text = cryptoMovement.name
//                date?.text = cryptoMovement.date
                type?.setText(
                    if (cryptoMovement.type.value == CryptoOperationType.SELL.value)
                        R.string.crypto_movement_operation_sell
                    else
                        R.string.crypto_movement_operation_buy
                )
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
                totalValue?.text =
                    FormatDecimalController.format(
                        cryptoMovement.totalValue,
                        cryptoMovement.totalValueCurrency
                    )
                tax?.text =
                    FormatDecimalController.format(
                        cryptoMovement.tax,
                        cryptoMovement.taxCurrency
                    )
                layout?.setOnClickListener {
                    navigation.goToUpdateCrypto(cryptoMovement)
                }
            }
        }
    }

    override fun getItemCount(): Int = listMovements.size

    fun relist(listToRelist: List<CryptoMovement>) {
        (listMovements as ArrayList).apply {
            clear()
            addAll(listToRelist)
        }

        notifyDataSetChanged()
    }

    class CryptoMovementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout: View? = itemView.findViewById(R.id.adapter_crypto_movement_layout)
        val deleteMovement: View? = itemView.findViewById(R.id.adapter_view_delete_crypto_movement)
        val name: TextView? = itemView.findViewById(R.id.adapter_crypto_movement_name)
        val date: TextView? = itemView.findViewById(R.id.adapter_crypto_movement_date)
        val type: TextView? = itemView.findViewById(R.id.adapter_crypto_movement_type)
        val boughtAmount: TextView? =
            itemView.findViewById(R.id.adapter_crypto_movement_bought_amount)
        val currentValue: TextView? =
            itemView.findViewById(R.id.adapter_crypto_movement_current_value)
        val totalValue: TextView? = itemView.findViewById(R.id.adapter_crypto_movement_total_value)
        val tax: TextView? = itemView.findViewById(R.id.adapter_crypto_movement_tax)
        val titleDate: TextView? = itemView.findViewById(R.id.adapter_crypto_movement_title_date)
    }

    companion object {
        private const val DATE_AND_CONTENT = 1
        private const val CONTENT = 0
    }
}
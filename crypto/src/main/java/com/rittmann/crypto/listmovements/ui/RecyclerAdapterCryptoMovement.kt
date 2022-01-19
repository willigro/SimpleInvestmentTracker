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
    private val list: List<CryptoMovement>,
    private val navigation: ListCryptoMovementsNavigation,
    private val onDeleteClicked: (CryptoMovement) -> Unit
) : RecyclerView.Adapter<RecyclerAdapterCryptoMovement.CryptoMovementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoMovementViewHolder {
        return CryptoMovementViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_crypto_movements, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CryptoMovementViewHolder, position: Int) {
        list[holder.adapterPosition].also { cryptoMovement ->
            holder.apply {

                deleteMovement?.setOnClickListener {
                    onDeleteClicked(cryptoMovement)
                }

                name?.text = cryptoMovement.name
                date?.text = cryptoMovement.date
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

    override fun getItemCount(): Int = list.size

    fun relist(listToRelist: List<CryptoMovement>) {
        (list as ArrayList).apply {
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
    }
}
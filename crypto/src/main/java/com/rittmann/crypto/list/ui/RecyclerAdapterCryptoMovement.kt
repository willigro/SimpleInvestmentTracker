package com.rittmann.crypto.list.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rittmann.crypto.R
import com.rittmann.common.datasource.basic.CryptoMovement

class RecyclerAdapterCryptoMovement(
    private val list: List<CryptoMovement>,
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
                name?.text = cryptoMovement.name
                date?.text = cryptoMovement.date
                type?.text = cryptoMovement.type.value
                boughtAmount?.text = cryptoMovement.boughtAmount.toString()
                currentValue?.text = cryptoMovement.currentValue.toString()
                totalValue?.text = cryptoMovement.totalValue.toString()
                tax?.text = cryptoMovement.tax.toString()
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
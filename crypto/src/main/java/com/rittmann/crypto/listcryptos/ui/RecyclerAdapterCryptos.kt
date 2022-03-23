package com.rittmann.crypto.listcryptos.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rittmann.crypto.R

class RecyclerAdapterCryptos(
    private val list: List<String>,
    private val listCryptosNavigation: ListCryptosNavigation
) : RecyclerView.Adapter<RecyclerAdapterCryptos.CryptoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        return CryptoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_cryptos, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        list[holder.adapterPosition].also { crypto ->
            holder.apply {
                layout?.setOnClickListener {
                    listCryptosNavigation.openCryptoResults(crypto)
                }
                name?.text = crypto
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun relist(listUpdated: List<String>) {
        (list as ArrayList<String>).apply {
            clear()
            addAll(listUpdated)
        }

        notifyDataSetChanged()
    }

    class CryptoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout: View? = itemView.findViewById(R.id.adapter_crypto)
        val name: TextView? = itemView.findViewById(R.id.adapter_crypto_name)
    }
}
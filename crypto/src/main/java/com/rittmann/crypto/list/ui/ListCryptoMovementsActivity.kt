package com.rittmann.crypto.list.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModelProvider
import com.rittmann.common.extensions.linearLayoutManager
import com.rittmann.common.lifecycle.BaseBindingActivity
import com.rittmann.common.viewmodel.viewModelProvider
import com.rittmann.crypto.R
import com.rittmann.crypto.databinding.ActivityListCryptoMovementsBinding
import com.rittmann.datasource.result.ResultEvent
import javax.inject.Inject

class ListCryptoMovementsActivity : BaseBindingActivity<ActivityListCryptoMovementsBinding>(
    R.layout.activity_list_crypto_movements
) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var listCryptoMovementsNavigation: ListCryptoMovementsNavigation

    @VisibleForTesting
    lateinit var viewModel: ListCryptoMovementsViewModel

    private var adapter: RecyclerAdapterCryptoMovement? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)
        binding.viewModel = viewModel

        initViews()
        initObservers()
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchAllCryptoMovements()
    }

    private fun initViews() {
        binding.apply {
            buttonRegisterNewCrypto.setOnClickListener {
                listCryptoMovementsNavigation.goToRegisterNewCrypto()
            }
        }
    }

    private fun initObservers() {
        viewModel.apply {
            cryptoMovementsList.observe(this@ListCryptoMovementsActivity, { result ->
                when (result) {
                    is ResultEvent.Success -> {
                        binding.apply {
                            if (adapter == null) {
                                adapter = RecyclerAdapterCryptoMovement(result.data)

                                recyclerCryptoMovement.linearLayoutManager()
                                recyclerCryptoMovement.adapter = adapter
                            } else {
                                adapter?.relist(result.data)
                            }
                        }
                    }
                    else -> {
                    }
                }
            })
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ListCryptoMovementsActivity::class.java))
        }
    }
}
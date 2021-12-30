package com.rittmann.crypto.listmovements.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModelProvider
import com.rittmann.common.viewmodel.viewModelProvider
import com.rittmann.crypto.R
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.extensions.linearLayoutManager
import com.rittmann.common.lifecycle.BaseFragmentBinding
import com.rittmann.crypto.databinding.FragmentListCryptoMovementsBinding
import com.rittmann.widgets.dialog.modal
import javax.inject.Inject

class ListCryptoMovementsFragment : BaseFragmentBinding<FragmentListCryptoMovementsBinding>(
    R.layout.fragment_list_crypto_movements
) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var listCryptoMovementsNavigation: ListCryptoMovementsNavigation

    @VisibleForTesting
    lateinit var viewModel: ListCryptoMovementsViewModel

    private var adapter: RecyclerAdapterCryptoMovement? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)
        binding.viewModel = viewModel

        initViews()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
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
            cryptoMovementsList.observe(this@ListCryptoMovementsFragment, { result ->
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
                        modal(
                            message = getString(R.string.list_crypto_error),
                            show = true,
                            ok = true,
                            cancelable = true
                        )
                    }
                }
            })
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ListCryptoMovementsFragment::class.java))
        }
    }
}
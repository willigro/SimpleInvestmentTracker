package com.rittmann.crypto.listcryptos.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.ViewModelProvider
import com.rittmann.common.datasource.basic.TradeMovementOperationTypeName
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.extensions.getDeviceHeightInPercentage
import com.rittmann.common.extensions.isNot
import com.rittmann.common.extensions.linearLayoutManager
import com.rittmann.common.extensions.visible
import com.rittmann.common.lifecycle.BaseFragmentBinding
import com.rittmann.common.viewmodel.viewModelProvider
import com.rittmann.crypto.R
import com.rittmann.crypto.databinding.FragmentListCryptosBinding
import javax.inject.Inject

class ListCryptosFragment :
    BaseFragmentBinding<FragmentListCryptosBinding>(R.layout.fragment_list_cryptos) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var listCryptosNavigation: ListCryptosNavigation

    @VisibleForTesting
    lateinit var viewModel: ListCryptosViewModel

    private var adapter: RecyclerAdapterCryptos? = null

    private val resultHeightPercentage: Float = 30.0F

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)
        binding.viewModel = viewModel

        initViews()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchCryptos()
    }

    private fun initViews() {
        binding.resultsContent.apply {
            cryptoMovementsResultBtnExpand.visible()

            containerCryptoResults.post {

                containerCryptoResults.tag = true
                containerCryptoResults.layoutParams = LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    requireActivity().getDeviceHeightInPercentage(resultHeightPercentage)
                )
            }

            cryptoMovementsResultBtnExpand.setOnClickListener {
                if (containerCryptoResults.tag == true) {
                    containerCryptoResults.tag = false
                    containerCryptoResults.layoutParams = LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                    )
                } else {
                    containerCryptoResults.tag = true
                    containerCryptoResults.layoutParams = LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        requireActivity().getDeviceHeightInPercentage(resultHeightPercentage)
                    )
                }
            }
        }
    }

    private fun initObservers() {
        viewModel.apply {
            cryptosList.observe(viewLifecycleOwner, { result ->
                when (result) {
                    is ResultEvent.Success -> loadList(result)
                    else -> {
                    }
                }
            })

            observeProgressPriority(this)
        }
    }

    private fun loadList(result: ResultEvent.Success<List<String>>) {
        binding.apply {
            val list =
                // Doing it for fun
                result.data.filter {
                    it.isNot(
                        TradeMovementOperationTypeName.WITHDRAW.value,
                        TradeMovementOperationTypeName.DEPOSIT.value
                    )
                }

            if (adapter == null) {
                adapter = RecyclerAdapterCryptos(list, listCryptosNavigation)

                recyclerCryptos.linearLayoutManager()
                recyclerCryptos.adapter = adapter
            } else {
                adapter?.relist(list)
            }
        }
    }
}

//infix fun <A> Boolean.andNot(that: A): Boolean = this && this != that

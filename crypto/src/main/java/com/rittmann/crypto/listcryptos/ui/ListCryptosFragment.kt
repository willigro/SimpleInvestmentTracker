package com.rittmann.crypto.listcryptos.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModelProvider
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.extensions.linearLayoutManager
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
        }
    }

    private fun loadList(result: ResultEvent.Success<List<String>>) {
        binding.apply {
            if (adapter == null) {
                adapter = RecyclerAdapterCryptos(result.data, listCryptosNavigation)

                recyclerCryptos.linearLayoutManager()
                recyclerCryptos.adapter = adapter
            } else {
                adapter?.relist(result.data)
            }
        }
    }
}
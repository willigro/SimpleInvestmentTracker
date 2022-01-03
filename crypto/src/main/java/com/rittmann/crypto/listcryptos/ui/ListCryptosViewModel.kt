package com.rittmann.crypto.listcryptos.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.lifecycle.BaseViewModelApp
import com.rittmann.crypto.listcryptos.domain.ListCryptosRepository
import com.rittmann.crypto.results.domain.CryptoResultsCalculate
import com.rittmann.crypto.results.ui.CryptoResultViewBinding
import javax.inject.Inject

class ListCryptosViewModel @Inject constructor(
    private val repository: ListCryptosRepository
) : BaseViewModelApp() {

    private val _cryptosList: MutableLiveData<ResultEvent<List<String>>> =
        MutableLiveData()
    val cryptosList: LiveData<ResultEvent<List<String>>>
        get() = _cryptosList

    var cryptoResultViewBinding: CryptoResultViewBinding = CryptoResultViewBinding()

    fun fetchCryptos() {
        executeAsyncThenMain(
            io = { repository.getAll() },
            main = { _cryptosList.value = it }
        )

        executeAsyncThenMain(
            io = { repository.fetchCryptos() },
            main = {
                if (it is ResultEvent.Success)
                    CryptoResultsCalculate.calculateResults(cryptoResultViewBinding, it.data)
            }
        )
    }
}
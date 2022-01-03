package com.rittmann.crypto.results.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.lifecycle.BaseViewModelApp
import com.rittmann.crypto.results.domain.CryptoResultsCalculate
import com.rittmann.crypto.results.domain.CryptoResultsRepository
import javax.inject.Inject

class CryptoResultsViewModel @Inject constructor(
    private val cryptoResultsRepository: CryptoResultsRepository
) : BaseViewModelApp() {

    private val _cryptosResult: MutableLiveData<ResultEvent<List<CryptoMovement>>> =
        MutableLiveData()
    val cryptosResult: LiveData<ResultEvent<List<CryptoMovement>>>
        get() = _cryptosResult

    var title: String = ""

    var cryptoResultViewBinding: CryptoResultViewBinding = CryptoResultViewBinding()

    fun fetchCryptos(cryptoName: String) {

        title = cryptoName

        executeAsyncThenMain({
            cryptoResultsRepository.fetchCryptos(cryptoName)
        }, {
            _cryptosResult.value = it

            if (it is ResultEvent.Success)
                CryptoResultsCalculate.calculateResults(cryptoResultViewBinding, it.data)
            else
                CryptoResultsCalculate.calculateResults(cryptoResultViewBinding, emptyList())
        })
    }
}
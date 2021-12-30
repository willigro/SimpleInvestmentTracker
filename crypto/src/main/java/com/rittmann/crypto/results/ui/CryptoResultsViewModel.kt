package com.rittmann.crypto.results.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.lifecycle.BaseViewModelApp
import com.rittmann.crypto.results.domain.CryptoResultsRepository
import javax.inject.Inject

class CryptoResultsViewModel @Inject constructor(
    private val cryptoResultsRepository: CryptoResultsRepository
) : BaseViewModelApp() {

    private val _cryptosResult: MutableLiveData<ResultEvent<List<CryptoMovement>>> =
        MutableLiveData()
    val cryptosResult: LiveData<ResultEvent<List<CryptoMovement>>>
        get() = _cryptosResult

    val cryptoResultViewBinding: CryptoResultViewBinding = CryptoResultViewBinding()

    fun fetchCryptos(cryptoName: String) {

        cryptoResultViewBinding.title.value = cryptoName

        executeAsyncThenMain({
            cryptoResultsRepository.fetchCryptos(cryptoName)
        }, {
            _cryptosResult.value = it

            if (it is ResultEvent.Success)
                calculateResults(it.data)
            else
                calculateResults(emptyList())
        })
    }

    private fun calculateResults(data: List<CryptoMovement>) {

        var earned = 0.0
        var invested = 0.0
        var boughtAmount = 0.0
        var soldAmount = 0.0
        var taxPaid = 0.0

        data.forEach {
            if (it.type == CryptoOperationType.BUY) {
                invested += it.totalValue
                boughtAmount += it.operatedAmount
                taxPaid += it.tax
            } else {
                earned += it.totalValue
                soldAmount += it.operatedAmount
                taxPaid += it.tax
            }
        }

        val onHand: Double = earned - invested
        val onHandAmount: Double = boughtAmount - soldAmount

        cryptoResultViewBinding.apply {
            totalOnHand.value = onHand.toString()
            totalEarned.value = earned.toString()
            totalInvested.value = invested.toString()
            totalBoughtAmount.value = boughtAmount.toString()
            totalSoldAmount.value = soldAmount.toString()
            totalOnHandAmount.value = onHandAmount.toString()
            totalTaxPaid.value = taxPaid.toString()
        }
    }
}

class CryptoResultViewBinding(
    val title: MutableLiveData<String> = MutableLiveData(),
    val totalOnHand: MutableLiveData<String> = MutableLiveData(),
    val totalEarned: MutableLiveData<String> = MutableLiveData(),
    val totalInvested: MutableLiveData<String> = MutableLiveData(),
    val totalBoughtAmount: MutableLiveData<String> = MutableLiveData(),
    val totalSoldAmount: MutableLiveData<String> = MutableLiveData(),
    val totalOnHandAmount: MutableLiveData<String> = MutableLiveData(),
    val totalTaxPaid: MutableLiveData<String> = MutableLiveData(),
)
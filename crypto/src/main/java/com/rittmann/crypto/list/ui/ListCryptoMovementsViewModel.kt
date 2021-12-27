package com.rittmann.crypto.list.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.rittmann.common.lifecycle.BaseViewModelApp
import com.rittmann.common.lifecycle.DispatcherProvider
import com.rittmann.crypto.list.domain.ListCryptoMovementsRepository
import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.result.ResultEvent
import javax.inject.Inject

class ListCryptoMovementsViewModel @Inject constructor(
    private val listCryptoMovementsRepositoryImpl: ListCryptoMovementsRepository,
    dispatcherProviderVm: DispatcherProvider
) : BaseViewModelApp(dispatcherProviderVm) {

    private val _cryptoMovementsList: MutableLiveData<ResultEvent<List<CryptoMovement>>> =
        MutableLiveData()
    val cryptoMovementsList: LiveData<ResultEvent<List<CryptoMovement>>>
        get() = Transformations.map(_cryptoMovementsList) { result ->
            calculateTotalValues(result)
            result
        }

    private val _totalValueInvested: MutableLiveData<String> = MutableLiveData()
    val totalValueInvested: MutableLiveData<String>
        get() = _totalValueInvested

    private val _totalValueEarned: MutableLiveData<String> = MutableLiveData()
    val totalValueEarned: MutableLiveData<String>
        get() = _totalValueEarned

    fun fetchAllCryptoMovements() {
        executeAsync {
            val result = listCryptoMovementsRepositoryImpl.getAll()

            executeMain {
                _cryptoMovementsList.value = result
            }
        }
    }

    private fun calculateTotalValues(result: ResultEvent<List<CryptoMovement>>?) {
        var totalInvested = 0.0
        var totalEarned = 0.0

        if (result is ResultEvent.Success) result.data.forEach { crypto ->
            if (crypto.type == CryptoOperationType.BUY) {
                totalInvested += crypto.totalValue
            } else
                totalEarned += crypto.totalValue
        }

        _totalValueInvested.value = totalInvested.toString()
        _totalValueEarned.value = totalEarned.toString()
    }
}
package com.rittmann.crypto.listmovements.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.rittmann.common.lifecycle.BaseViewModelApp
import com.rittmann.common.lifecycle.DispatcherProvider
import com.rittmann.crypto.listmovements.domain.ListCryptoMovementsRepository
import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.basic.CurrencyType
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.extensions.orZero
import com.rittmann.common.utils.EditDecimalFormatController
import com.rittmann.common.utils.FormatDecimalController
import com.rittmann.common.utils.transformerIt
import com.rittmann.crypto.results.domain.CryptoResultsCalculate
import javax.inject.Inject

class ListCryptoMovementsViewModel @Inject constructor(
    private val repository: ListCryptoMovementsRepository,
    dispatcherProviderVm: DispatcherProvider
) : BaseViewModelApp(dispatcherProviderVm) {

    private val _tradeMovementsList: MutableLiveData<ResultEvent<List<TradeMovement>>> =
        MutableLiveData()
    val tradeMovementsList: LiveData<ResultEvent<List<TradeMovement>>>
        get() = Transformations.map(_tradeMovementsList) { result ->
            calculateTotalValues(result)
            result
        }

    private val _totalValueInvested: MutableLiveData<Double> = MutableLiveData()
    val totalValueInvested: LiveData<String>
        get() = transformerIt(_totalValueInvested) {
            FormatDecimalController.format(
                it.orZero(),
                CurrencyType.REAL,
                EditDecimalFormatController.SCALE_LIMIT
            )
        }

    private val _totalValueEarned: MutableLiveData<Double> = MutableLiveData()
    val totalValueEarned: LiveData<String>
        get() = transformerIt(_totalValueEarned) {
            FormatDecimalController.format(
                it.orZero(),
                CurrencyType.REAL,
                EditDecimalFormatController.SCALE_LIMIT
            )
        }

    private val _totalValueOnHand: MutableLiveData<Double> = MutableLiveData()
    val totalValueOnHand: LiveData<String>
        get() = transformerIt(_totalValueOnHand) {
            FormatDecimalController.format(
                it.orZero(),
                CurrencyType.REAL,
                EditDecimalFormatController.SCALE_LIMIT
            )
        }

    private val _totalValueDeposit: MutableLiveData<Double> = MutableLiveData()
    val totalValueDeposit: LiveData<String>
        get() = transformerIt(_totalValueDeposit) {
            FormatDecimalController.format(
                it.orZero(),
                CurrencyType.REAL,
                EditDecimalFormatController.SCALE_LIMIT
            )
        }

    private val _cryptoMovementDeleted: MutableLiveData<ResultEvent<Int>> = MutableLiveData()
    val cryptoMovementDeleted: LiveData<ResultEvent<Int>>
        get() = _cryptoMovementDeleted

    fun fetchAllCryptoMovements() {
        showProgress()
        executeAsyncThenMainSuspend(
            io = {
                repository.getAll()
            },
            main = {
                _tradeMovementsList.value = it
            },
            progress = true
        )
    }

    private fun calculateTotalValues(result: ResultEvent<List<TradeMovement>>?) {
        var totalInvested = 0.0
        var totalEarned = 0.0
        var totalDeposited = 0.0

        if (result is ResultEvent.Success) result.data.forEach { crypto ->
            when (crypto.type) {
                CryptoOperationType.BUY -> totalInvested += crypto.calculateTotalValue()
                CryptoOperationType.SELL -> totalEarned += crypto.calculateTotalValue()
                CryptoOperationType.DEPOSIT -> totalDeposited += crypto.calculateTotalValue()
            }
        }

        _totalValueInvested.value = totalInvested
        _totalValueEarned.value = totalEarned
        _totalValueDeposit.value = totalDeposited
        _totalValueOnHand.value = CryptoResultsCalculate.calculateTotalOnHand(totalDeposited, totalEarned, totalInvested)
    }

    fun deleteCrypto(tradeMovementToDelete: TradeMovement) {
        executeAsyncThenMainSuspend(
            io = {
                repository.delete(tradeMovementToDelete)
            },
            main = {
                _cryptoMovementDeleted.value = it
            },
            progress = true
        )
    }
}
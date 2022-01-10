package com.rittmann.crypto.listmovements.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.rittmann.common.lifecycle.BaseViewModelApp
import com.rittmann.common.lifecycle.DispatcherProvider
import com.rittmann.crypto.listmovements.domain.ListCryptoMovementsRepository
import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.basic.CurrencyType
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.extensions.orZero
import com.rittmann.common.utils.EditDecimalFormatController
import com.rittmann.common.utils.FormatDecimalController
import com.rittmann.common.utils.transformerIt
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
                totalInvested += crypto.calculateTotalValue()
            } else
                totalEarned += crypto.calculateTotalValue()
        }

        _totalValueInvested.value = totalInvested
        _totalValueEarned.value = totalEarned
    }
}
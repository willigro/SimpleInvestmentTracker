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
import com.rittmann.common.extensions.indexOfBy
import com.rittmann.common.extensions.orZero
import com.rittmann.common.utils.EditDecimalFormatController
import com.rittmann.common.utils.FormatDecimalController
import com.rittmann.common.utils.pagination.PageInfo
import com.rittmann.common.utils.transformerIt
import com.rittmann.crypto.results.domain.CryptoResultsCalculate
import javax.inject.Inject

class ListCryptoMovementsViewModel @Inject constructor(
    private val repository: ListCryptoMovementsRepository,
    dispatcherProviderVm: DispatcherProvider
) : BaseViewModelApp(dispatcherProviderVm) {

    private val _tradeMovementsList: MutableLiveData<List<TradeMovement>> =
        MutableLiveData()
    val tradeMovementsList: LiveData<List<TradeMovement>>
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

    private val _totalValueWithdraw: MutableLiveData<Double> = MutableLiveData()
    val totalValueWithdraw: LiveData<String>
        get() = transformerIt(_totalValueWithdraw) {
            FormatDecimalController.format(
                it.orZero(),
                CurrencyType.REAL,
                EditDecimalFormatController.SCALE_LIMIT
            )
        }

    private val _cryptoMovementDeleted: MutableLiveData<ResultEvent<TradeMovement>> =
        MutableLiveData()
    val cryptoMovementDeleted: LiveData<ResultEvent<TradeMovement>>
        get() = _cryptoMovementDeleted

    var pageInfo: PageInfo<TradeMovement> = PageInfo()

    fun fetchAllCryptoMovements(next: Boolean) {
        pageInfo.setEnableRefresh(PageInfo.PageState.LOADING)
        showProgress()
        executeAsyncThenMainSuspend(
            io = {
                repository.getAll(pageInfo.getNextPage(next))
            },
            main = {

                val pageResult = if (it is ResultEvent.Success) {
                    if (it.data.isEmpty())
                        PageInfo.PageResult(isEndList = true)
                    else
                        PageInfo.PageResult(it.data)
                } else
                    PageInfo.PageResult(isEndList = true)

                _tradeMovementsList.value = pageInfo.getResult(pageResult)

                pageInfo.setEnableRefresh(PageInfo.PageState.IDLE)
            },
            progress = true
        )
    }

    private fun calculateTotalValues(result: List<TradeMovement>?) {
        var totalInvested = 0.0
        var totalEarned = 0.0
        var totalDeposited = 0.0
        var totalWithdraw = 0.0

        result?.forEach { crypto ->
            when (crypto.type) {
                CryptoOperationType.BUY -> totalInvested += crypto.calculateTotalValue()
                CryptoOperationType.SELL -> totalEarned += crypto.calculateTotalValue()
                CryptoOperationType.DEPOSIT -> totalDeposited += crypto.calculateTotalValue()
                CryptoOperationType.WITHDRAW -> totalWithdraw += crypto.calculateTotalValue()
            }
        }

        _totalValueInvested.value = totalInvested
        _totalValueEarned.value = totalEarned
        _totalValueDeposit.value = totalDeposited
        _totalValueWithdraw.value = totalWithdraw
        _totalValueOnHand.value =
            CryptoResultsCalculate.calculateTotalOnHand(totalDeposited, totalEarned, totalInvested, totalWithdraw)
    }

    fun deleteCrypto(tradeMovementToDelete: TradeMovement) {
        executeAsyncThenMainSuspend(
            io = {
                repository.delete(tradeMovementToDelete)
            },
            main = {
                if (it is ResultEvent.Success) {
                    val list = _tradeMovementsList.value ?: arrayListOf()

                    (list as ArrayList).remove(tradeMovementToDelete)

                    _tradeMovementsList.value = list

                    calculateTotalValues(list)
                }

                _cryptoMovementDeleted.value = it
            },
            progress = true
        )
    }

    fun tradeMovementWasUpdated(data: TradeMovement) {
        val index = pageInfo.completeList.indexOfBy {
            it.id == data.id
        }
        if (index != -1)
            (pageInfo.completeList as ArrayList).apply {
                this[index] = data
                _tradeMovementsList.value = sortedByDescending { it.date }

                calculateTotalValues(this)
            }
    }

    fun tradeMovementWasInserted(data: TradeMovement) {
        pageInfo.apply {
            addOnlyIfNotContains(data)
            (completeList as ArrayList).apply {
                val arr = completeList.sortedByDescending { it.date }
                clear()
                addAll(arr)
            }
            _tradeMovementsList.value = completeList

            calculateTotalValues(completeList)
        }
    }
}
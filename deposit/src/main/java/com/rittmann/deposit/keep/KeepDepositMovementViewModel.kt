package com.rittmann.deposit.keep

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.basic.TradeMovementOperationTypeName
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.lifecycle.BaseViewModelApp
import com.rittmann.deposit.domain.KeepDepositRepository
import java.util.*
import javax.inject.Inject

class KeepDepositMovementViewModel @Inject constructor(
    private val repositoryImpl: KeepDepositRepository
) : BaseViewModelApp() {

    var tradeMovement: MutableLiveData<TradeMovement> = MutableLiveData(TradeMovement.deposit())

    private val _registerResultEvent: MutableLiveData<ResultEvent<TradeMovement>> =
        MutableLiveData<ResultEvent<TradeMovement>>()
    val registerResultEvent: LiveData<ResultEvent<TradeMovement>> get() = _registerResultEvent

    private val _updateResultEvent: MutableLiveData<ResultEvent<TradeMovement>> =
        MutableLiveData<ResultEvent<TradeMovement>>()
    val updateResultEvent: LiveData<ResultEvent<TradeMovement>> get() = _updateResultEvent

    fun saveTrade() {
        tradeMovement.value?.also { tradeMovement ->
            tradeMovement.calculateTotalValueToDeposit()

            if (tradeMovement.cryptoCoin.isEmpty())
                tradeMovement.updateCryptoCoinWithName()

            if (tradeMovement.isInserting()) {
                insertDeposit(tradeMovement)
            } else
                updateDeposit(tradeMovement)
        }
    }

    private fun insertDeposit(tradeMovement: TradeMovement) {
        executeAsyncThenMainSuspend(
            io = {
                repositoryImpl.register(tradeMovement)
            },
            main = {
                _registerResultEvent.value = it
            },
            progress = true
        )
    }

    private fun updateDeposit(tradeMovement: TradeMovement) {
        executeAsyncThenMainSuspend(
            io = {
                repositoryImpl.update(tradeMovement)
            },
            main = {
                _updateResultEvent.value = it
            },
            progress = true
        )
    }

    fun changeDate(calendar: Calendar) {
        tradeMovement.value?.also {
            it.date = calendar
        }
    }

    fun attachTradeMovementForUpdate(tradeMovement: TradeMovement?) {
        tradeMovement?.also {
            this.tradeMovement.value = tradeMovement
        }
    }

    fun onTradeoOperationTypeChanged(type: CryptoOperationType) {
        tradeMovement.value?.apply {
            this.type = type
            if (type == CryptoOperationType.DEPOSIT)
                name = TradeMovementOperationTypeName.DEPOSIT.value
            else if (type == CryptoOperationType.WITHDRAW)
                name = TradeMovementOperationTypeName.WITHDRAW.value
        }
    }
}
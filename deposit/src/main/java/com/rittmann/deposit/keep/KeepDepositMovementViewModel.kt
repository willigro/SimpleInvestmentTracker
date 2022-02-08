package com.rittmann.deposit.keep

import androidx.lifecycle.MutableLiveData
import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.lifecycle.BaseViewModelApp
import com.rittmann.deposit.domain.KeepDepositRepository
import java.util.*
import javax.inject.Inject

class KeepDepositMovementViewModel @Inject constructor(
    private val repositoryImpl: KeepDepositRepository
) : BaseViewModelApp() {

    var tradeMovement: MutableLiveData<TradeMovement> = MutableLiveData(TradeMovement.deposit())

    fun saveTrade() {
        tradeMovement.value?.also {
            executeAsyncThenMainSuspend(
                io = {
                    it.currentValue = it.totalValue
                    repositoryImpl.register(it)
                },
                main = {

                },
                progress = true
            )
        }
    }

    fun changeDate(calendar: Calendar) {
        tradeMovement.value?.also {
            it.date = calendar
        }
    }
}
package com.rittmann.crypto.keep.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rittmann.common.lifecycle.BaseViewModelApp
import com.rittmann.common.lifecycle.DispatcherProvider
import com.rittmann.crypto.keep.domain.RegisterCryptoMovementRepository
import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.result.ResultEvent
import java.util.*
import javax.inject.Inject

class RegisterCryptoMovementViewModel @Inject constructor(
    private val repository: RegisterCryptoMovementRepository,
    dispatcherProviderVm: DispatcherProvider
) : BaseViewModelApp(dispatcherProviderVm) {

    private val _registerResultEvent: MutableLiveData<ResultEvent<TradeMovement>> =
        MutableLiveData<ResultEvent<TradeMovement>>()
    val registerResultEvent: LiveData<ResultEvent<TradeMovement>> get() = _registerResultEvent

    private val _updateResultEvent: MutableLiveData<ResultEvent<TradeMovement>> =
        MutableLiveData<ResultEvent<TradeMovement>>()
    val updateResultEvent: LiveData<ResultEvent<TradeMovement>> get() = _updateResultEvent

    private val _cryptoNamesResultEvent: MutableLiveData<ResultEvent<List<String>>> =
        MutableLiveData<ResultEvent<List<String>>>()
    val cryptoNamesResultEvent: LiveData<ResultEvent<List<String>>> get() = _cryptoNamesResultEvent

    private val _lastTradeResultEvent: MutableLiveData<ResultEvent<TradeMovement>> =
        MutableLiveData<ResultEvent<TradeMovement>>()
    val lastTradeResultEvent: LiveData<ResultEvent<TradeMovement>> get() = _lastTradeResultEvent

    private val _dateRetrieved: MutableLiveData<Calendar> =
        MutableLiveData<Calendar>()
    val dateRetrieved: LiveData<Calendar> get() = _dateRetrieved

    var tradeMovement: MutableLiveData<TradeMovement> = MutableLiveData(TradeMovement())

    fun attachCryptoMovementForUpdate(tradeMovement: TradeMovement?) {
        tradeMovement?.also {
            this.tradeMovement.value = tradeMovement
        }
    }

    fun saveCrypto() {
        showProgress()

        tradeMovement.value?.updateCryptoCoinWithName()

        if (tradeMovement.value?.isInserting() == true)
            insertNewCrypto()
        else
            updateCrypto()
    }

    fun fetchCryptos(nameLike: String) {
        if (nameLike.isEmpty())
            _cryptoNamesResultEvent.postValue(ResultEvent.Success(arrayListOf()))
        else
            executeAsyncThenMainSuspend(
                io = {
                    repository.fetchCryptoNames(nameLike)
                },
                main = {
//                    println("Testing $it")
                    _cryptoNamesResultEvent.value = it
                }
            )
    }

    fun fetchLastCrypto(name: String) {
        executeAsyncThenMainSuspend(
            io = {
                repository.fetchLastCrypto(name)
            },
            main = {
                _lastTradeResultEvent.value = it
            }
        )
    }

    private fun insertNewCrypto() {
        tradeMovement.value?.also {
            executeAsyncThenMainSuspend(
                io = {
                    repository.registerCrypto(it)
                },
                main = { result ->
                    _registerResultEvent.value = result

                    if (result is ResultEvent.Success)
                        tradeMovement.value?.id = result.data.id

                    hideProgress()
                }
            )
        } ?: hideProgress()
    }

    private fun updateCrypto() {
        tradeMovement.value?.also {
            executeAsyncThenMainSuspend(
                io = {
                    repository.updateCrypto(it)
                },
                main = {
                    _updateResultEvent.value = it

                    hideProgress()
                }
            )
        } ?: hideProgress()
    }

    fun onCryptoOperationTypeChanged(type: CryptoOperationType) {
        tradeMovement.value?.type = type
    }

    fun changeDate(calendar: Calendar) {
        tradeMovement.value?.date = calendar
    }

    fun retrieveDate() {
        _dateRetrieved.postValue(tradeMovement.value?.date)
    }
}
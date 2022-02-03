package com.rittmann.crypto.keep.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rittmann.common.lifecycle.BaseViewModelApp
import com.rittmann.common.lifecycle.DispatcherProvider
import com.rittmann.crypto.keep.domain.RegisterCryptoMovementRepository
import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.result.ResultEvent
import java.util.*
import javax.inject.Inject

class RegisterCryptoMovementViewModel @Inject constructor(
    private val repository: RegisterCryptoMovementRepository,
    dispatcherProviderVm: DispatcherProvider
) : BaseViewModelApp(dispatcherProviderVm) {

    private val _registerResultEvent: MutableLiveData<ResultEvent<CryptoMovement>> =
        MutableLiveData<ResultEvent<CryptoMovement>>()
    val registerResultEvent: LiveData<ResultEvent<CryptoMovement>> get() = _registerResultEvent

    private val _updateResultEvent: MutableLiveData<ResultEvent<Int>> =
        MutableLiveData<ResultEvent<Int>>()
    val updateResultEvent: LiveData<ResultEvent<Int>> get() = _updateResultEvent

    private val _cryptoNamesResultEvent: MutableLiveData<ResultEvent<List<String>>> =
        MutableLiveData<ResultEvent<List<String>>>()
    val cryptoNamesResultEvent: LiveData<ResultEvent<List<String>>> get() = _cryptoNamesResultEvent

    private val _lastCryptoResultEvent: MutableLiveData<ResultEvent<CryptoMovement>> =
        MutableLiveData<ResultEvent<CryptoMovement>>()
    val lastCryptoResultEvent: LiveData<ResultEvent<CryptoMovement>> get() = _lastCryptoResultEvent

    private val _dateRetrieved: MutableLiveData<Calendar> =
        MutableLiveData<Calendar>()
    val dateRetrieved: LiveData<Calendar> get() = _dateRetrieved

    var cryptoMovement: MutableLiveData<CryptoMovement> = MutableLiveData(CryptoMovement())

    fun attachCryptoMovementForUpdate(cryptoMovement: CryptoMovement?) {
        cryptoMovement?.also {
            this.cryptoMovement.value = cryptoMovement
        }
    }

    fun saveCrypto() {
        showProgress()
        if (cryptoMovement.value?.isInserting() == true)
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
                _lastCryptoResultEvent.value = it
            }
        )
    }

    private fun insertNewCrypto() {
        cryptoMovement.value?.also {
            executeAsyncThenMainSuspend(
                io = {
                    repository.registerCrypto(it)
                },
                main = { result ->
                    _registerResultEvent.value = result

                    if (result is ResultEvent.Success)
                        cryptoMovement.value?.id = result.data.id

                    hideProgress()
                }
            )
        } ?: hideProgress()
    }

    private fun updateCrypto() {
        cryptoMovement.value?.also {
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
        cryptoMovement.value?.type = type
    }

    fun changeDate(calendar: Calendar) {
        cryptoMovement.value?.date = calendar
    }

    fun retrieveDate() {
        _dateRetrieved.postValue(cryptoMovement.value?.date)
    }
}
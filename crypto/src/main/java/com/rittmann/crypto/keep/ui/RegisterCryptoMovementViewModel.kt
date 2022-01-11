package com.rittmann.crypto.keep.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rittmann.common.lifecycle.BaseViewModelApp
import com.rittmann.common.lifecycle.DispatcherProvider
import com.rittmann.crypto.keep.domain.RegisterCryptoMovementRepository
import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.result.ResultEvent
import javax.inject.Inject

class RegisterCryptoMovementViewModel @Inject constructor(
    private val registerCryptoMovementRepository: RegisterCryptoMovementRepository,
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

    private val _dateRetrieved: MutableLiveData<String> =
        MutableLiveData<String>()
    val dateRetrieved: LiveData<String> get() = _dateRetrieved

    var cryptoMovement: MutableLiveData<CryptoMovement> = MutableLiveData(CryptoMovement())

    fun attachCryptoMovementForUpdate(cryptoMovement: CryptoMovement?) {
        cryptoMovement?.also {
            this.cryptoMovement.value = cryptoMovement
        }
    }

    fun saveCrypto() {
        showProgress()
        executeAsync {
            if (cryptoMovement.value?.id == 0L)
                insertNewCrypto()
            else
                updateCrypto()
        }
    }

    fun fetchCryptos(nameLike: String) {
        if (nameLike.isEmpty())
            _cryptoNamesResultEvent.postValue(ResultEvent.Success(arrayListOf()))
        else
            executeAsyncThenMainSuspend(
                io = {
                    registerCryptoMovementRepository.fetchCryptoNames(nameLike)
                },
                main = {
                    _cryptoNamesResultEvent.value = it
                }
            )
    }

    private suspend fun insertNewCrypto() {
        cryptoMovement.value?.also {
            val result = registerCryptoMovementRepository.registerCrypto(it)

            executeMain {
                _registerResultEvent.value = result

                if (result is ResultEvent.Success)
                    cryptoMovement.value?.id = result.data.id

                hideProgress()
            }
        } ?: hideProgress()
    }

    private suspend fun updateCrypto() {
        cryptoMovement.value?.also {
            val result = registerCryptoMovementRepository.updateCrypto(it)

            executeMain {
                _updateResultEvent.value = result

                hideProgress()
            }
        } ?: hideProgress()
    }

    fun onCryptoOperationTypeChanged(type: CryptoOperationType) {
        cryptoMovement.value?.type = type
    }

    fun changeDate(date: String) {
        cryptoMovement.value?.date = date
    }

    fun retrieveDate() {
        _dateRetrieved.postValue(cryptoMovement.value?.date.orEmpty())
    }
}
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
        cryptoMovement.value = cryptoMovement.value
    }
}
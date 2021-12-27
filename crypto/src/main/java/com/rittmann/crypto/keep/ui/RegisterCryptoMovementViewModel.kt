package com.rittmann.crypto.keep.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rittmann.common.lifecycle.BaseViewModelApp
import com.rittmann.common.lifecycle.DispatcherProvider
import com.rittmann.crypto.keep.domain.RegisterCryptoMovementRepository
import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.result.ResultEvent
import javax.inject.Inject

class RegisterCryptoMovementViewModel @Inject constructor(
    private val registerCryptoMovementRepository: RegisterCryptoMovementRepository,
    dispatcherProviderVm: DispatcherProvider
) : BaseViewModelApp(dispatcherProviderVm) {

    private val _registerResultEvent: MutableLiveData<ResultEvent<CryptoMovement>> =
        MutableLiveData<ResultEvent<CryptoMovement>>()
    val registerResultEvent: LiveData<ResultEvent<CryptoMovement>> get() = _registerResultEvent

    val cryptoMovement: CryptoMovement = CryptoMovement()

    fun registerCrypto() {
        showProgress()
        executeAsync {
            val result = registerCryptoMovementRepository.registerCrypto(cryptoMovement)

            executeMain {
                _registerResultEvent.value = result
            }

            if (result is ResultEvent.Success)
                cryptoMovement.id = result.data.id

            hideProgress()
        }
    }
}
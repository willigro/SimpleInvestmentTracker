package com.rittmann.crypto.keep.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rittmann.baselifecycle.base.BaseViewModel
import com.rittmann.datasource.basic.CryptoMovement
import com.rittmann.crypto.keep.domain.RegisterCryptoMovementRepository
import com.rittmann.datasource.result.ResultEvent
import javax.inject.Inject

class RegisterCryptoMovementViewModel @Inject constructor(
    private val registerCryptoMovementRepository: RegisterCryptoMovementRepository
) : BaseViewModel() {

    private val _registerResultEvent: MutableLiveData<ResultEvent<CryptoMovement>> =
        MutableLiveData<ResultEvent<CryptoMovement>>()
    val registerResultEvent: LiveData<ResultEvent<CryptoMovement>> get() = _registerResultEvent

    val cryptoMovement: CryptoMovement = CryptoMovement()

    fun registerCrypto() {
        showProgress()
        executeAsync {
            with(registerCryptoMovementRepository.registerCrypto(cryptoMovement)) {
                _registerResultEvent.value = this

                if (this is ResultEvent.Success)
                    cryptoMovement.id = this.data.id
            }

            hideProgress()
        }
    }
}
package com.rittmann.crypto.list.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rittmann.common.lifecycle.BaseViewModelApp
import com.rittmann.common.lifecycle.DispatcherProvider
import com.rittmann.crypto.list.domain.ListCryptoMovementsRepository
import com.rittmann.datasource.basic.CryptoMovement
import com.rittmann.datasource.result.ResultEvent
import javax.inject.Inject

class ListCryptoMovementsViewModel @Inject constructor(
    private val listCryptoMovementsRepositoryImpl: ListCryptoMovementsRepository,
    dispatcherProviderVm: DispatcherProvider
) : BaseViewModelApp(dispatcherProviderVm) {

    private val _cryptoMovementsList: MutableLiveData<ResultEvent<List<CryptoMovement>>> =
        MutableLiveData()
    val cryptoMovementsList: LiveData<ResultEvent<List<CryptoMovement>>>
        get() = _cryptoMovementsList

    fun fetchAllCryptoMovements() {
        executeAsync {
            val result = listCryptoMovementsRepositoryImpl.getAll()

            executeMain {
                _cryptoMovementsList.value = result
            }
        }
    }
}
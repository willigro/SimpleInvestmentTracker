package com.rittmann.crypto.listcryptos.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.lifecycle.BaseViewModelApp
import com.rittmann.crypto.listcryptos.domain.ListCryptosRepository
import javax.inject.Inject

class ListCryptosViewModel @Inject constructor(
    private val repository: ListCryptosRepository
) : BaseViewModelApp() {

    private val _cryptosList: MutableLiveData<ResultEvent<List<String>>> =
        MutableLiveData()
    val cryptosList: LiveData<ResultEvent<List<String>>>
        get() = _cryptosList

    fun fetchCryptos() {
        executeAsyncThenMain(
            io = { repository.getAll() },
            main = { _cryptosList.value = it }
        )
    }
}
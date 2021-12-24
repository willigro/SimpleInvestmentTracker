package com.rittmann.common.lifecycle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rittmann.baselifecycle.livedata.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *
 * Copy from Robbie
 * @author Willi
 *
 * */
open class BaseViewModelApp(
    var dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {

    var viewModelScopeGen: CoroutineScope? = null
    protected var _progress = MutableLiveData<Boolean>()
    protected var _errorCon = SingleLiveEvent<Void>()
    protected var _errorGen = SingleLiveEvent<Void>()

    val errorCon get() = _errorCon
    val errorGen get() = _errorGen
    val isLoading get() = _progress

    protected fun showProgress() {
        _progress.postValue(true)
    }

    protected fun hideProgress() {
        _progress.postValue(false)
    }

    fun forceHideProgress() {
        _progress.postValue(false)
    }

    open fun handleGenericFailure() {
        _errorGen.call()
    }

    open fun handleConnectionFailure() {
        _errorCon.call()
    }

    open fun clearViewModel() {
        _errorCon = SingleLiveEvent()
        _errorGen = SingleLiveEvent()
    }

    fun executeAsyncProgress(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        scope: CoroutineScope = viewModelScope,
        block: suspend () -> Unit
    ) {
        val s = viewModelScopeGen ?: scope
        s.launch {
            withContext(dispatcher) {
                _progress.postValue(true)
                block()
                _progress.postValue(false)
            }
        }
    }

    protected fun executeAsync(
            dispatcherProvider: DispatcherProvider = this.dispatcherProvider,
            dispatcher: CoroutineDispatcher = Dispatchers.IO,
            scope: CoroutineScope = viewModelScope,
            block: suspend () -> Unit
    ) {
        val dp = when (dispatcher) {
            Dispatchers.IO -> dispatcherProvider.io()
            Dispatchers.Main -> dispatcherProvider.main()
            Dispatchers.Default -> dispatcherProvider.default()
            Dispatchers.Unconfined -> dispatcherProvider.unconfined()
            else -> dispatcherProvider.io()
        }
        (viewModelScopeGen ?: scope).launch(dp) {
            block()
        }
    }

    fun executeMain(
            scope: CoroutineScope = viewModelScope,
            block: suspend () -> Unit
    ) {
        (viewModelScopeGen ?: scope).launch(dispatcherProvider.main()) {
            block()
        }
    }
}

interface DispatcherProvider {

    fun main(): CoroutineDispatcher = Dispatchers.Main
    fun default(): CoroutineDispatcher = Dispatchers.Default
    fun io(): CoroutineDispatcher = Dispatchers.IO
    fun unconfined(): CoroutineDispatcher = Dispatchers.Unconfined
}

class DefaultDispatcherProvider : DispatcherProvider

package com.rittmann.common.lifecycle

import androidx.lifecycle.viewModelScope
import com.rittmann.baselifecycle.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *
 * Copy from Robbie
 * @author Willi
 *
 * */
open class BaseViewModelApp(
    var dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider()
) : BaseViewModel() {

    fun forceHideProgress() {
        _progress.postValue(false)
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

    fun <T> executeAsyncThenMain(
        io: () -> T,
        main: (result: T) -> Unit,
        progress: Boolean = false
    ) {
        if (progress)
            showProgress()

        executeAsync {
            val result = io()

            executeMain {
                main(result)

                if (progress)
                    hideProgress()
            }
        }
    }

    fun <T> executeAsyncThenMainSuspend(
        io: suspend () -> T,
        main: (result: T) -> Unit,
        progress: Boolean = false
    ) {
        if (progress)
            showProgress()
        executeAsync {
            val result = io()

            executeMain {
                main(result)

                if (progress)
                    hideProgress()
            }
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

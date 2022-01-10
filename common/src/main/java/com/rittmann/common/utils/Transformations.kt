package com.rittmann.common.utils

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData


@MainThread
fun <X, Y> transformerIt(
    source: LiveData<X>,
    mapFunction: (X?) -> Y
): LiveData<Y> {
    val result = MediatorLiveData<Y>()
    result.addSource(
        source
    ) { result.value = mapFunction(source.value) }
    return result
}
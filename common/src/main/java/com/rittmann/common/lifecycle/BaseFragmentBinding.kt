package com.rittmann.common.lifecycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dagger.android.support.DaggerAppCompatDialogFragment

abstract class BaseFragmentBinding<T : ViewDataBinding>(private val resId: Int) : DaggerAppCompatDialogFragment() {
    protected lateinit var binding: T

    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DataBindingUtil.inflate<T>(inflater, resId, container, false).let {
        binding = it
        rootView = it.root
        it.lifecycleOwner = viewLifecycleOwner
        it.root
    }
}
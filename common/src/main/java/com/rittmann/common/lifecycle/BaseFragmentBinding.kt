package com.rittmann.common.lifecycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rittmann.baselifecycle.base.BaseActivity
import dagger.android.support.DaggerAppCompatDialogFragment

abstract class BaseFragmentBinding<T : ViewDataBinding>(
    private val resId: Int,
    private val resIdContainer: Int = -1
) :
    DaggerAppCompatDialogFragment() {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as BaseActivity).resIdViewReference = resIdContainer
    }

    fun showProgress() {
        (requireActivity() as BaseActivity).showProgress()
    }

    fun hideProgress() {
        (requireActivity() as BaseActivity).hideProgress()
    }

    fun observeProgress(viewModelApp: BaseViewModelApp){
        viewModelApp.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading == true) {
                showProgress()
            } else {
                hideProgress()
            }
        })
    }
}
package com.rittmann.crypto.keep.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModelProvider
import com.rittmann.common.extensions.toast
import com.rittmann.common.lifecycle.BaseBindingActivity
import com.rittmann.common.validations.FieldValidation
import com.rittmann.common.viewmodel.viewModelProvider
import com.rittmann.crypto.R
import com.rittmann.crypto.databinding.ActivityRegisterCryptoMovementBinding
import com.rittmann.datasource.result.ResultEvent
import javax.inject.Inject

class RegisterCryptoMovementActivity :
    BaseBindingActivity<ActivityRegisterCryptoMovementBinding>(R.layout.activity_register_crypto_movement) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var registerCryptoNavigation: RegisterCryptoNavigation

    @VisibleForTesting
    lateinit var movementViewModel: RegisterCryptoMovementViewModel

    private var field: FieldValidation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movementViewModel = viewModelProvider(viewModelFactory)
        binding.viewModel = movementViewModel

        initViews()
        initObservers()
    }

    private fun initObservers() {
        movementViewModel.apply {
            registerResultEvent.observe(this@RegisterCryptoMovementActivity, {
                when (it) {
                    is ResultEvent.Success -> {
                        toast(getString(R.string.new_crypto_movement_was_registered))

                        registerCryptoNavigation.close()
                    }
                    else -> {
                        toast(getString(R.string.new_crypto_movement_was_registered))
                    }
                }
            })
        }
    }

    private fun initViews() {
        binding.apply {
            field = FieldValidation(btnRegister).apply {
                add(editText = editCryptoName.editText) {
                    it.isNotEmpty()
                }

                start()

                runValidations()
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, RegisterCryptoMovementActivity::class.java))
        }
    }
}
package com.rittmann.crypto.keep.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModelProvider
import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.extensions.toast
import com.rittmann.common.lifecycle.BaseBindingActivity
import com.rittmann.common.validations.FieldValidation
import com.rittmann.common.viewmodel.viewModelProvider
import com.rittmann.crypto.R
import com.rittmann.crypto.databinding.ActivityRegisterCryptoMovementBinding
import com.rittmann.common.datasource.result.ResultEvent
import javax.inject.Inject

class RegisterCryptoMovementActivity :
    BaseBindingActivity<ActivityRegisterCryptoMovementBinding>(R.layout.activity_register_crypto_movement) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var registerCryptoNavigation: RegisterCryptoNavigation

    @VisibleForTesting
    lateinit var viewModel: RegisterCryptoMovementViewModel

    private var field: FieldValidation? = null

    private val cryptoMovement: CryptoMovement? by lazy {
        intent?.extras?.getSerializable(CRYPTO_MOVEMENT) as CryptoMovement?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)
        binding.viewModel = viewModel

        initViews()
        initObservers()

        viewModel.attachCryptoMovementForUpdate(cryptoMovement)
    }

    private fun initObservers() {
        viewModel.apply {
            registerResultEvent.observe(this@RegisterCryptoMovementActivity, {
                when (it) {
                    is ResultEvent.Success -> {
                        toast(getString(R.string.new_crypto_movement_was_registered))
                    }
                    else -> {
                        toast(getString(R.string.new_crypto_movement_was_not_registered))
                    }
                }
            })

            updateResultEvent.observe(this@RegisterCryptoMovementActivity, {
                when (it) {
                    is ResultEvent.Success -> {
                        toast(getString(R.string.new_crypto_movement_was_updated))
                    }
                    else -> {
                        toast(getString(R.string.new_crypto_movement_was_not_updated))
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

        private const val CRYPTO_MOVEMENT = "cm"

        fun start(context: Context) {
            context.startActivity(Intent(context, RegisterCryptoMovementActivity::class.java))
        }

        fun start(context: Context, cryptoMovement: CryptoMovement) {
            Intent(context, RegisterCryptoMovementActivity::class.java).apply {
                putExtra(CRYPTO_MOVEMENT, cryptoMovement)
                context.startActivity(this)
            }
        }
    }
}
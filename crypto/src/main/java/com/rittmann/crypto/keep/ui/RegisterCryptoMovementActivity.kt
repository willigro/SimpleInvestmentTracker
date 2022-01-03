package com.rittmann.crypto.keep.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModelProvider
import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.extensions.linearLayoutManager
import com.rittmann.common.extensions.toast
import com.rittmann.common.lifecycle.BaseBindingActivity
import com.rittmann.common.utils.EditTextSearch
import com.rittmann.common.validations.FieldValidation
import com.rittmann.common.viewmodel.viewModelProvider
import com.rittmann.crypto.R
import com.rittmann.crypto.databinding.ActivityRegisterCryptoMovementBinding
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

    private var adapter: RecyclerAdapterSearchCryptos? = null

    private var editTextSearch: EditTextSearch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)
        binding.viewModel = viewModel

        initViews()
        initObservers()

        viewModel.attachCryptoMovementForUpdate(cryptoMovement)
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


            editCryptoName.editText?.also { edit ->
                editTextSearch = EditTextSearch(edit) {
                    this@RegisterCryptoMovementActivity.viewModel.fetchCryptos(it)
                }
            }
        }
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

            cryptoNamesResultEvent.observe(this@RegisterCryptoMovementActivity, {
                when (it) {
                    is ResultEvent.Success -> {
                        configureListResult(it.data)
                    }
                    else -> {
                    }
                }
            })
        }
    }

    private fun configureListResult(data: List<String>) {
        if (adapter == null) {
            adapter = RecyclerAdapterSearchCryptos(data) {
                editTextSearch?.changeValue(it)
                adapter?.hide()
            }

            binding.listViewCryptoNameResults.linearLayoutManager()
            binding.listViewCryptoNameResults.adapter = adapter
        } else {
            adapter?.relist(data)
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
package com.rittmann.deposit.keep

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModelProvider
import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.extensions.toDoubleValid
import com.rittmann.common.extensions.toast
import com.rittmann.common.lifecycle.BaseBindingActivity
import com.rittmann.common.utils.DateListenerUtil
import com.rittmann.common.utils.DateUtil
import com.rittmann.common.validations.FieldValidation
import com.rittmann.common.viewmodel.viewModelProvider
import com.rittmann.deposit.R
import com.rittmann.deposit.databinding.ActivityKeepDepositBinding
import javax.inject.Inject

class KeepDepositActivity :
    BaseBindingActivity<ActivityKeepDepositBinding>(R.layout.activity_keep_deposit) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

//    @Inject
//    lateinit var registerCryptoNavigation: RegisterCryptoNavigation

    @VisibleForTesting
    lateinit var viewModel: KeepDepositMovementViewModel

    private var field: FieldValidation? = null

    private val dateListenerUtil = DateListenerUtil()

    private val tradeMovement: TradeMovement by lazy {
        (intent?.extras?.getSerializable(DEPOSIT_MOVEMENT) as TradeMovement?)
            ?: TradeMovement.deposit()
    }

    private var wasInserted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)
        binding.viewModel = viewModel

        viewModel.attachTradeMovementForUpdate(tradeMovement)

        initViews()
        initObservers()
    }

    private fun initViews() {
        setupToolbarAndButton()

        dateListenerUtil.configureListener { calendar ->
            DateUtil.simpleDateFormat(calendar).also {
                binding.txtCryptoDate.text = it
                viewModel.changeDate(calendar)
            }
        }

        tradeMovement.also {
            dateListenerUtil.calendar = it.date
            DateUtil.simpleDateFormat(it.date).also { date ->
                binding.txtCryptoDate.text = date
            }
        }

        binding.apply {
            field = FieldValidation(btnRegister).apply {
                add(editText = editCryptoTotalValue.editText) {
                    editCryptoTotalValue.editDecimalFormatController?.normalCurrency()
                        ?.toDoubleValid() ?: 0.0 > 0.0
                }

                start()

                runValidations()
            }

            txtCryptoDate.apply {
                setOnClickListener {
                    dateListenerUtil.show(this@KeepDepositActivity)
                }

                text = DateUtil.simpleDateFormat(dateListenerUtil.calendar)
            }

        }
    }

    private fun setupToolbarAndButton() {
        configureToolbar(getString(R.string.deposit_title))

        binding.btnRegister.text = if (tradeMovement.isInserting()) {
            getString(R.string.deposit_btn_register)
        } else {
            getString(R.string.deposit_btn_update)
        }
    }

    private fun initObservers() {
        viewModel.apply {
            registerResultEvent.observe(this@KeepDepositActivity, {
                when (it) {
                    is ResultEvent.Success -> {
                        toast(getString(R.string.deposit_was_registered))
                        Intent().apply {
                            putExtra(DEPOSIT_MOVEMENT_RESULT_INSERTED, it.data)
                            setResult(Activity.RESULT_OK, this)
                        }
                        wasInserted = true
                        this@KeepDepositActivity.tradeMovement.id = it.data.id
                        setupToolbarAndButton()
                    }
                    else -> {
                        toast(getString(R.string.deposit_was_not_registered))
                    }
                }
            })

            updateResultEvent.observe(this@KeepDepositActivity, {
                when (it) {
                    is ResultEvent.Success -> {
                        toast(getString(R.string.deposit_was_updated))

                        if (wasInserted.not())
                            Intent().apply {
                                putExtra(DEPOSIT_MOVEMENT_RESULT_UPDATED, it.data)
                                setResult(Activity.RESULT_OK, this)
                            }
                    }
                    else -> {
                        toast(getString(R.string.deposit_was_not_updated))
                    }
                }
            })
        }
    }

    companion object {
        private const val DEPOSIT_MOVEMENT = "dm"
        const val DEPOSIT_MOVEMENT_RESULT_INSERTED = "dmi"
        const val DEPOSIT_MOVEMENT_RESULT_UPDATED = "dmu"

        fun getIntent(context: Context, tradeMovement: TradeMovement? = null): Intent {
            return Intent(context, KeepDepositActivity::class.java).apply {
                putExtra(DEPOSIT_MOVEMENT, tradeMovement)
            }
        }
    }
}
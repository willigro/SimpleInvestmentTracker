package com.rittmann.crypto.keep.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.extensions.linearLayoutManager
import com.rittmann.common.extensions.toDoubleValid
import com.rittmann.common.extensions.toast
import com.rittmann.common.lifecycle.BaseBindingActivity
import com.rittmann.common.utils.DateUtil
import com.rittmann.common.utils.EditTextSearch
import com.rittmann.common.validations.FieldValidation
import com.rittmann.common.viewmodel.viewModelProvider
import com.rittmann.crypto.R
import com.rittmann.crypto.databinding.ActivityRegisterCryptoMovementBinding
import java.util.*
import javax.inject.Inject

class RegisterCryptoMovementActivity
    : BaseBindingActivity<ActivityRegisterCryptoMovementBinding>(
    R.layout.activity_register_crypto_movement
) {

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

    private var calendar: Calendar = Calendar.getInstance()

    private val dateListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            DateUtil.simpleDateFormat(calendar).also {
                binding.txtCryptoDate.text = it
                viewModel.changeDate(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)
        binding.viewModel = viewModel
        viewModel.attachCryptoMovementForUpdate(cryptoMovement)

        initViews()
        initObservers()
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

            txtCryptoDate.apply {
                DateUtil.simpleDateFormat(calendar).also {
                    text = it

                    if (isInserting())
                        this@RegisterCryptoMovementActivity.viewModel.changeDate(it)
                    else
                        this@RegisterCryptoMovementActivity.viewModel.retrieveDate()
                }

                setOnClickListener {
                    DatePickerDialog(
                        this@RegisterCryptoMovementActivity,
                        dateListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            }

            editCryptoCurrentValue.editDecimalFormatController?.onChangeScale?.observeForever {
                if (checkboxUpdateTotalValue.isChecked)
                    editCryptoTotalValue.editDecimalFormatController?.changeScale(it)
            }

            editCryptoCurrentValue.editDecimalFormatController?.onChangeValue?.observeForever {
                if (checkboxUpdateTotalValue.isChecked) {
                    val amount = editCryptoBoughtAmount.editText?.text.toString().toDoubleValid()
                    val currentValue =
                        editCryptoCurrentValue.editDecimalFormatController?.normalCurrency().toDoubleValid()

                    editCryptoTotalValue.editDecimalFormatController?.setCurrency(amount * currentValue)
                }
            }

            editCryptoBoughtAmount.editText?.doAfterTextChanged {
                if (checkboxUpdateTotalValue.isChecked) {
                    val amount = it.toString().toDoubleValid()
                    val currentValue =
                        editCryptoCurrentValue.editDecimalFormatController?.normalCurrency().toDoubleValid()

                    editCryptoTotalValue.editDecimalFormatController?.setCurrency(amount * currentValue)
                }
            }
        }
    }

    private fun isInserting(): Boolean =
        cryptoMovement == null || cryptoMovement?.isInserting() == true

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
                if (it is ResultEvent.Success)
                    configureListResult(it.data)
            })

            dateRetrieved.observe(this@RegisterCryptoMovementActivity, {
                binding.txtCryptoDate.text = it
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
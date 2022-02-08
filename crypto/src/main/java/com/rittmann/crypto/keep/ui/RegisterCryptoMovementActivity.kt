package com.rittmann.crypto.keep.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.rittmann.common.customs.currencyType
import com.rittmann.common.customs.valueScale
import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.extensions.linearLayoutManager
import com.rittmann.common.extensions.toast
import com.rittmann.common.lifecycle.BaseBindingActivity
import com.rittmann.common.utils.DateUtil
import com.rittmann.common.utils.EditTextSearch
import com.rittmann.common.validations.FieldValidation
import com.rittmann.common.viewmodel.viewModelProvider
import com.rittmann.crypto.R
import com.rittmann.crypto.databinding.ActivityRegisterCryptoMovementBinding
import java.math.BigDecimal
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

    private val tradeMovement: TradeMovement? by lazy {
        intent?.extras?.getSerializable(CRYPTO_MOVEMENT) as TradeMovement?
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
                viewModel.changeDate(calendar)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)
        binding.viewModel = viewModel
        viewModel.attachCryptoMovementForUpdate(tradeMovement)

        initViews()
        initObservers()
    }

    @SuppressLint("ClickableViewAccessibility")
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
                    if (edit.tag == true)
                        this@RegisterCryptoMovementActivity.viewModel.fetchCryptos(it)
                    else
                        edit.tag = true
                }

                edit.setOnFocusChangeListener { _, b ->
                    if (b) {
                        getAllChildren(scrollViewContainer).forEach { v ->
                            if (v.id != editCryptoName.id)
                                v.setOnTouchListener { _, _ ->
                                    adapter?.hide()
                                    v.performClick()
                                    v.setOnTouchListener(null)
                                    true
                                }
                        }
                    }
                }
            }

            txtCryptoDate.apply {
                text = DateUtil.simpleDateFormat(calendar)

                if (isInserting())
                    this@RegisterCryptoMovementActivity.viewModel.changeDate(calendar)
                else
                    this@RegisterCryptoMovementActivity.viewModel.retrieveDate()

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

            configureReactiveViews()
        }
    }

    private fun getAllChildren(v: View): List<View> {
        if (v !is ViewGroup) {
            val viewArrayList: ArrayList<View> = ArrayList<View>()
            viewArrayList.add(v)
            return viewArrayList
        }
        val result: ArrayList<View> = ArrayList<View>()
        for (i in 0 until v.childCount) {
            val child: View = v.getChildAt(i)

            result.addAll(getAllChildren(child))
        }
        return result
    }

    private fun configureReactiveViews() {
        binding.apply {
            editCryptoCurrentValue.editDecimalFormatController?.onChangeScale?.observeForever {
                if (checkboxUpdateTotalValue.isChecked)
                    editCryptoTotalValue.editDecimalFormatController?.changeScale(it)
            }

//            editCryptoCurrentValue.editDecimalFormatController?.onChangeCurrencyType?.observeForever {
//                if (checkboxUpdateTotalValue.isChecked)
//                    editCryptoTotalValue.editDecimalFormatController?.setCurrencyType(it)
//            }

            editCryptoCurrentValue.editDecimalFormatController?.onChangeValue?.observeForever {
                if (checkboxUpdateTotalValue.isChecked) {
                    updateTheTotalValue()
                }
            }

            editCryptoBoughtAmount.editText?.doAfterTextChanged {
                if (checkboxUpdateTotalValue.isChecked) {
                    updateTheTotalValue()
                }
            }
        }
    }

    private fun updateTheTotalValue() {
        binding.apply {
            val amount = editCryptoBoughtAmount.editDecimalFormatController?.normalCurrency()
                ?: BigDecimal(0.0)

            val currentValue =
                editCryptoCurrentValue.editDecimalFormatController?.normalCurrency()
                    ?: BigDecimal(0.0)

            editCryptoTotalValue.editDecimalFormatController?.setCurrency(amount.times(currentValue))
        }
    }

    private fun isInserting(): Boolean =
        tradeMovement == null || tradeMovement?.isInserting() == true

    private fun initObservers() {
        viewModel.apply {
            registerResultEvent.observe(this@RegisterCryptoMovementActivity, {
                when (it) {
                    is ResultEvent.Success -> {
                        toast(getString(R.string.new_crypto_movement_was_registered))
                        setResult(Activity.RESULT_OK)
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
                        setResult(Activity.RESULT_OK)
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

            lastTradeResultEvent.observe(this@RegisterCryptoMovementActivity, {
                if (it is ResultEvent.Success)
                    updateTheScalesAndCurrencies(it.data)
            })

            dateRetrieved.observe(this@RegisterCryptoMovementActivity, {
                binding.txtCryptoDate.text = DateUtil.simpleDateFormat(it)
            })
        }
    }

    private fun updateTheScalesAndCurrencies(data: TradeMovement) {
        binding.apply {
            editCryptoCurrentValue.valueScale(data.currentValue.scale())

            editCryptoTotalValue.valueScale(data.totalValue.scale())

            editCryptoBoughtAmount.valueScale(data.operatedAmount.scale())

            editCryptoTax.valueScale(data.tax.scale())
            editCryptoTax.currencyType(data.taxCurrency)
        }
    }

    private fun configureListResult(data: List<String>) {
        if (adapter == null) {
            adapter = RecyclerAdapterSearchCryptos(data) {
                editTextSearch?.changeValue(it)
                adapter?.hide()
                viewModel.fetchLastCrypto(it)
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
            context.startActivity(getIntent(context))
        }

        fun start(context: Context, tradeMovement: TradeMovement) {
            context.startActivity(getIntent(context, tradeMovement))
        }

        fun getIntent(context: Context) =
            Intent(context, RegisterCryptoMovementActivity::class.java)

        fun getIntent(context: Context, tradeMovement: TradeMovement) =
            Intent(context, RegisterCryptoMovementActivity::class.java).apply {
                putExtra(CRYPTO_MOVEMENT, tradeMovement)
            }
    }
}
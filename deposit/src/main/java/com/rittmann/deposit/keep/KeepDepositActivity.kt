package com.rittmann.deposit.keep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModelProvider
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)
        binding.viewModel = viewModel

        initViews()
    }

    private fun initViews() {
        dateListenerUtil.configureListener { calendar ->
            DateUtil.simpleDateFormat(calendar).also {
                binding.txtCryptoDate.text = it
                viewModel.changeDate(calendar)
            }
        }

        binding.apply {
            txtCryptoDate.apply {
                setOnClickListener {
                    dateListenerUtil.show(this@KeepDepositActivity)
                }

                text = DateUtil.simpleDateFormat(dateListenerUtil.calendar)
            }

        }
    }

    companion object {
        fun start(context: Context) {
            Intent(context, KeepDepositActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }
}
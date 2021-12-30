package com.rittmann.crypto.results.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModelProvider
import com.rittmann.common.lifecycle.BaseBindingActivity
import com.rittmann.common.viewmodel.viewModelProvider
import com.rittmann.crypto.R
import com.rittmann.crypto.databinding.ActivityCryptoResultsBinding
import javax.inject.Inject

class CryptoResultsActivity : BaseBindingActivity<ActivityCryptoResultsBinding>(
    R.layout.activity_crypto_results
) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var cryptoResultsNavigation: CryptoResultsNavigation

    @VisibleForTesting
    lateinit var viewModel: CryptoResultsViewModel

    private val cryptoName: String by lazy {
        intent?.extras?.getString(CRYPTO).orEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)
        binding.viewModel = viewModel

        initViews()
        initObservers()

        viewModel.fetchCryptos(cryptoName)
    }

    private fun initViews() {

    }

    private fun initObservers() {

    }

    companion object {
        private const val CRYPTO = "c"

        fun intent(context: Context, cryptoMovementName: String) =
            Intent(context, CryptoResultsActivity::class.java).apply {
                putExtra(CRYPTO, cryptoMovementName)
            }

        fun start(context: Context, cryptoMovementName: String) {
            context.startActivity(intent(context, cryptoMovementName))
        }
    }
}
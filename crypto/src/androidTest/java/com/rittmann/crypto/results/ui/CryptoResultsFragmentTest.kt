package com.rittmann.crypto.results.ui

import androidx.test.core.app.ApplicationProvider
import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.dao.interfaces.CryptoDao
import com.rittmann.common_test.EspressoUtil.checkValue
import com.rittmann.crypto.BaseTestActivity
import com.rittmann.crypto.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CryptoResultsFragmentTest : BaseTestActivity() {

    private val cryptoDao: CryptoDao? = dataBase?.cryptoDao()

    @Before
    fun setup() {
        cryptoDao?.deleteAll()
    }

    @Test
    fun loadAnExistentCurrency_BTC_Success() = runBlockingTest {
        val name = "BTC"

        val crypto = CryptoMovement(
            name = "BTC",
            date = "00/00/0000",
            type = CryptoOperationType.BUY,
            operatedAmount = 10.0,
            currentValue = 1.0,
            totalValue = 10.0,
            tax = 1.0,
        )
        val list = arrayListOf(
            crypto,
            crypto.copy(
                type = CryptoOperationType.BUY,
                operatedAmount = 15.0,
                currentValue = 2.0,
                totalValue = 30.0
            ),
            crypto.copy(
                type = CryptoOperationType.SELL,
                operatedAmount = 5.0,
                currentValue = 3.0,
                totalValue = 15.0
            )
        )

        cryptoDao?.insert(list)

        val activityScenario = getActivity<CryptoResultsFragment>(
            intent = CryptoResultsFragment.intent(
                ApplicationProvider.getApplicationContext(),
                name
            ),
            monitor = true
        )

        checkValue(R.id.txt_crypto_results_title, name)
        checkValue(R.id.txt_crypto_results_total_earned, "15.0")
        checkValue(R.id.txt_crypto_results_total_invested, "40.0")
        checkValue(R.id.txt_crypto_results_total_on_hand, "-25.0")
        checkValue(R.id.txt_crypto_results_total_bought_amount, "25.0")
        checkValue(R.id.txt_crypto_results_total_sold_amount, "5.0")
        checkValue(R.id.txt_crypto_results_total_on_hand_amount, "20.0")
        checkValue(R.id.txt_crypto_results_total_tax_paid, "3.0")

        activityScenario.close()
    }

    @Test
    fun loadAnNonexistentCurrency_XYC_Success() = runBlockingTest {
        val name = "XYC"

        val activityScenario = getActivity<CryptoResultsFragment>(
            intent = CryptoResultsFragment.intent(
                ApplicationProvider.getApplicationContext(),
                name
            ),
            monitor = true
        )

        checkValue(R.id.txt_crypto_results_title, name)
        checkValue(R.id.txt_crypto_results_total_earned, "0.0")
        checkValue(R.id.txt_crypto_results_total_invested, "0.0")
        checkValue(R.id.txt_crypto_results_total_on_hand, "0.0")
        checkValue(R.id.txt_crypto_results_total_bought_amount, "0.0")
        checkValue(R.id.txt_crypto_results_total_sold_amount, "0.0")
        checkValue(R.id.txt_crypto_results_total_on_hand_amount, "0.0")
        checkValue(R.id.txt_crypto_results_total_tax_paid, "0.0")

        activityScenario.close()
    }
}
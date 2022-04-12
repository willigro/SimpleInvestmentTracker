package com.rittmann.crypto.keep.ui

import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.rittmann.common.customs.CustomEditTextCurrency
import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.utils.DateUtil
import com.rittmann.common.utils.EditDecimalFormatController
import com.rittmann.common_test.getOrAwaitValue
import com.rittmann.crypto.BaseActivityTest
import com.rittmann.crypto.R
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import java.util.Calendar
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test

class RegisterCryptoMovementActivityTest : BaseActivityTest<RegisterCryptoMovementActivity>() {

    @MockK
    private lateinit var observerTradeMovement: Observer<TradeMovement>

    @Before
    fun setupActivity() {
        MockKAnnotations.init(this)
        configureActivity<RegisterCryptoMovementActivity>()
    }

    @Test
    fun t() {
        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -1)
        }

        forceResume()

        activity.viewModel.changeDate(yesterday)
        activity.viewModel.retrieveDate()

        activity.viewModel.dateRetrieved.getOrAwaitValue()
        assertThat(
            activity.binding.txtCryptoDate.text.toString(),
            `is`(DateUtil.simpleDateFormat(yesterday))
        )

        assertThat(activity.binding.btnRegister.visibility, `is`(View.VISIBLE))
    }

    @Test
    fun `checking the first configurations of the screen when it is the insert flow`() {
        activityController.create()

        val viewModel = activity.viewModel

        every { observerTradeMovement.onChanged(any()) } returns Unit

        viewModel.tradeMovement.observeForever(observerTradeMovement)

        // todo: check if the tradeLiveData was changed
        activity.binding.apply {
            val toolbar = root.findViewById<TextView>(R.id.toolbar_title).text.toString()
            assertThat(toolbar, `is`(getString(R.string.trader_crypto_title)))

            activityController.start()
            activityController.resume()

            assertThat(
                txtCryptoDate.text.toString(),
                `is`(DateUtil.simpleDateFormat(Calendar.getInstance()))
            )

            assertThat(editCryptoName.editText?.text.toString(), `is`(""))

            assertThat(radioCryptoOperationTypeBuy.isChecked, `is`(true))
            assertThat(radioCryptoOperationTypeSell.isChecked, `is`(false))
            assertThat(checkboxUpdateTotalValue.isChecked, `is`(true))
            assertThat(checkboxUpdateTotalValueCalculateAfterTransaction.isChecked, `is`(true))
            assertThat(btnRegister.isEnabled, `is`(false))
            assertThat(btnRegister.text, `is`(getString(R.string.trade_movement_btn_register)))

            assertThat(editCryptoBoughtAmount.editText?.text.toString(), `is`("0,00000"))
            assertThat(
                editCryptoBoughtAmount.editDecimalFormatController?.scale, `is`(
                    EditDecimalFormatController.DEFAULT_SCALE
                )
            )

            val defaultValueCurrencyReal = "R\$ 0,00000" // Space
            val defaultValueCurrencyCrypto = "C 0,00000"

            assertCurrencyAndScale(editCryptoConcreteTotalValue, defaultValueCurrencyReal)
            assertCurrencyAndScale(editCryptoTotalValue, defaultValueCurrencyReal)
            assertCurrencyAndScale(editCryptoCurrentValue, defaultValueCurrencyReal)
            assertCurrencyAndScale(editCryptoTax, defaultValueCurrencyCrypto)
        }

        verify(exactly = 1) { observerTradeMovement.onChanged(any()) }
    }

    private fun assertCurrencyAndScale(
        customEdit: CustomEditTextCurrency,
        text: String,
        scale: Int = EditDecimalFormatController.DEFAULT_SCALE
    ) {
        assertThat(customEdit.editText?.text.toString(), `is`(text))
        assertThat(
            customEdit.editDecimalFormatController?.scale, `is`(
                scale
            )
        )
    }
}
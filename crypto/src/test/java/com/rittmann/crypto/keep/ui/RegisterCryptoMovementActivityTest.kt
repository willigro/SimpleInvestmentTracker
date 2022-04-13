package com.rittmann.crypto.keep.ui

import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.rittmann.common.customs.CustomEditTextCurrency
import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.datasource.result.succeeded
import com.rittmann.common.lifecycle.DefaultDispatcherProvider
import com.rittmann.common.utils.DateUtil
import com.rittmann.common.utils.EditDecimalFormatController
import com.rittmann.common.utils.EditTextSearch
import com.rittmann.common_test.MainCoroutineRule
import com.rittmann.common_test.getOrAwaitValue
import com.rittmann.common_test.mock.exceptionMock
import com.rittmann.crypto.BaseActivityTest
import com.rittmann.crypto.R
import com.rittmann.crypto.keep.domain.RegisterCryptoMovementRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import java.util.Calendar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegisterCryptoMovementActivityTest : BaseActivityTest<RegisterCryptoMovementActivity>() {

    @MockK
    private lateinit var observerTradeMovement: Observer<TradeMovement>

    @MockK
    private lateinit var observerCryptoNames: Observer<ResultEvent<List<String>>>

    @MockK
    private lateinit var repository: RegisterCryptoMovementRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

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

    /**
     * Inserting
     * */

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

    /**
     * Loading names
     * */

    @Test
    fun `typing a name and loading a list of crypto names`() {
        activityController.create()

        activity.setTestViewModel(
            RegisterCryptoMovementViewModel(
                repository,
                DefaultDispatcherProvider()
            )
        )

        val names = arrayListOf(
            "A",
            "B"
        )
        every { repository.fetchCryptoNames(any()) } returns ResultEvent.Success(names)

        every { observerCryptoNames.onChanged(any()) } returns Unit

        activity.viewModel.cryptoNamesResultEvent.observeForever(observerCryptoNames)

        forceResumeFromCreate()

        val searchFor = "ABC"

        // The first one will unblock the flow
        Thread.sleep(EditTextSearch.DELAY_TEST)
        activity.binding.editCryptoName.setTextEditText(searchFor)

        val result =
            activity.viewModel.cryptoNamesResultEvent.getOrAwaitValue(EditTextSearch.DELAY_TEST)

        assertThat(result.succeeded, `is`(true))
        assertThat((result as ResultEvent.Success).data.size, `is`(2))

        // item count is null
        assertThat(activity.adapter?.itemCount, `is`(2))
    }

    @Test
    fun `typing three names and loading a list then replace that list with another and do the same for the third one`() {
        activityController.create()

        activity.setTestViewModel(
            RegisterCryptoMovementViewModel(
                repository,
                DefaultDispatcherProvider()
            )
        )

        val nameOneSearch = "ABC"
        val namesOne = arrayListOf(
            "A",
            "B"
        )
        val nameTwoSearch = "123"
        val namesTwo = arrayListOf(
            "1",
            "2",
            "3"
        )
        val nameThreeSearch = ""

        every { observerCryptoNames.onChanged(any()) } returns Unit

        /**
         * First
         * */
        every { repository.fetchCryptoNames(nameOneSearch) } returns ResultEvent.Success(namesOne)

        activity.viewModel.cryptoNamesResultEvent.observeForever(observerCryptoNames)

        forceResumeFromCreate()

        // The first one will unblock the flow
        Thread.sleep(EditTextSearch.DELAY_TEST)
        activity.binding.editCryptoName.setTextEditText(nameOneSearch)

        val resultOne =
            activity.viewModel.cryptoNamesResultEvent.getOrAwaitValue(EditTextSearch.DELAY_TEST)

        assertThat(resultOne.succeeded, `is`(true))
        assertThat((resultOne as ResultEvent.Success).data.size, `is`(2))

        // item count is null
        assertThat(activity.adapter?.itemCount, `is`(2))

        /**
         * Second
         * */
        every { repository.fetchCryptoNames(nameTwoSearch) } returns ResultEvent.Success(namesTwo)

        activity.viewModel.cryptoNamesResultEvent.observeForever(observerCryptoNames)

        forceResumeFromCreate()

        // The first one will unblock the flow
        Thread.sleep(EditTextSearch.DELAY_TEST)
        activity.binding.editCryptoName.setTextEditText(nameTwoSearch)

        val resultTwo =
            activity.viewModel.cryptoNamesResultEvent.getOrAwaitValue(EditTextSearch.DELAY_TEST)

        assertThat(resultTwo.succeeded, `is`(true))
        assertThat((resultTwo as ResultEvent.Success).data.size, `is`(3))

        // item count is null
        assertThat(activity.adapter?.itemCount, `is`(3))

        /**
         * Third
         * */
        every { repository.fetchCryptoNames(any()) } returns ResultEvent.Success(namesTwo)

        activity.viewModel.cryptoNamesResultEvent.observeForever(observerCryptoNames)

        forceResumeFromCreate()

        // The first one will unblock the flow
        Thread.sleep(EditTextSearch.DELAY_TEST)
        activity.binding.editCryptoName.setTextEditText(nameThreeSearch)

        val resultThree =
            activity.viewModel.cryptoNamesResultEvent.getOrAwaitValue(EditTextSearch.DELAY_TEST)

        assertThat(resultThree.succeeded, `is`(true))
        assertThat((resultThree as ResultEvent.Success).data.size, `is`(0))

        // item count is null
        assertThat(activity.adapter?.itemCount, `is`(0))

    }

    @Test
    fun `typing a name but then returns an error`() {
        activityController.create()

        activity.setTestViewModel(
            RegisterCryptoMovementViewModel(
                repository,
                DefaultDispatcherProvider()
            )
        )

        every { repository.fetchCryptoNames(any()) } returns ResultEvent.Error(exceptionMock)

        every { observerCryptoNames.onChanged(any()) } returns Unit

        activity.viewModel.cryptoNamesResultEvent.observeForever(observerCryptoNames)

        forceResumeFromCreate()

        val searchFor = "ABC"

        // The first one will unblock the flow
        Thread.sleep(EditTextSearch.DELAY_TEST)
        activity.binding.editCryptoName.setTextEditText(searchFor)

        val result =
            activity.viewModel.cryptoNamesResultEvent.getOrAwaitValue(EditTextSearch.DELAY_TEST)

        assertThat(result.succeeded, `is`(false))
        assertThat((result as ResultEvent.Error).exception, `is`(exceptionMock))

        // item count is null
        assertThat(activity.adapter?.itemCount, `is`(0))
    }

    /**
     * Helpers
     * */

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
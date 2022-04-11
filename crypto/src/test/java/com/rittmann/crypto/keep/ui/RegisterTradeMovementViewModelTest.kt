package com.rittmann.crypto.keep.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.datasource.result.succeeded
import com.rittmann.common.extensions.isNot
import com.rittmann.common.lifecycle.DefaultDispatcherProvider
import com.rittmann.common_test.MainCoroutineRule
import com.rittmann.common_test.getOrAwaitValue
import com.rittmann.common_test.mock.exceptionMock
import com.rittmann.common_test.mock.newCryptoMovementMock
import com.rittmann.common_test.mock.registeredCryptoMovementMock
import com.rittmann.crypto.keep.domain.RegisterCryptoMovementRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import java.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.nullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RegisterTradeMovementViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: RegisterCryptoMovementViewModel

    @MockK
    private lateinit var movementRepository: RegisterCryptoMovementRepository

    private val delayForLoading: Long = 300

    @Before
    fun setupViewModel() {
        MockKAnnotations.init(this)

        viewModel = RegisterCryptoMovementViewModel(
            movementRepository,
            DefaultDispatcherProvider()
        )
    }

    @After
    fun cleanViewModel() {
        viewModel.clearViewModel()
    }

    /**
     * Insert
     * */

    @Test
    fun `register a new crypto currency and handle the success case`() {
        mainCoroutineRule.pauseDispatcher()

        coEvery { movementRepository.registerCrypto(any()) } returns ResultEvent.Success(
            registeredCryptoMovementMock
        )

        viewModel.saveCrypto()

        assertThat(viewModel.isLoading.getOrAwaitValue(), `is`(true))

        mainCoroutineRule.resumeDispatcher()

        val resultEvent = viewModel.registerResultEvent.getOrAwaitValue()

        assertThat(resultEvent.succeeded, `is`(true))

        resultEvent as ResultEvent.Success
        resultEvent.data.also { result ->
            assertThat(result.id, greaterThan(0L))
            assertThat(result.id, `is`(registeredCryptoMovementMock.id))
            assertThat(result.date, `is`(registeredCryptoMovementMock.date))
            assertThat(result.name, `is`(registeredCryptoMovementMock.name))
            assertThat(result.operatedAmount, `is`(registeredCryptoMovementMock.operatedAmount))
            assertThat(result.totalValue, `is`(registeredCryptoMovementMock.totalValue))
            assertThat(result.currentValue, `is`(registeredCryptoMovementMock.currentValue))
            assertThat(result.tax, `is`(registeredCryptoMovementMock.tax))
        }

        assertThat(viewModel.isLoading.getOrAwaitValue(delayForLoading), `is`(false))
    }

    @Test
    fun `try to register a new crypto currency and handle an error case`() {
        mainCoroutineRule.pauseDispatcher()

        coEvery { movementRepository.registerCrypto(any()) } returns ResultEvent.Error(
            exceptionMock
        )

        viewModel.saveCrypto()

        assertThat(viewModel.isLoading.getOrAwaitValue(), `is`(true))

        mainCoroutineRule.resumeDispatcher()

        val resultEvent = viewModel.registerResultEvent.getOrAwaitValue()

        assertThat(resultEvent.succeeded, `is`(false))

        resultEvent as ResultEvent.Error
        assertThat(resultEvent.exception, `is`(exceptionMock))

        assertThat(viewModel.isLoading.getOrAwaitValue(delayForLoading), `is`(false))
    }

    /**
     * Update
     * */

    @Test
    fun `update a crypto movement and handler its success case`() {
        mainCoroutineRule.pauseDispatcher()

        val name = "Testing"

        val crypto = newCryptoMovementMock.copy(
            id = 1L,
            name = name
        )

        viewModel.attachCryptoMovementForUpdate(crypto)

        coEvery { movementRepository.updateCrypto(any()) } returns ResultEvent.Success(crypto)

        viewModel.saveCrypto()

        assertThat(viewModel.isLoading.getOrAwaitValue(), `is`(true))

        mainCoroutineRule.resumeDispatcher()

        val resultEvent = viewModel.updateResultEvent.getOrAwaitValue()

        assertThat(resultEvent.succeeded, `is`(true))
        assertThat((resultEvent as ResultEvent.Success).data.name, `is`(name))
        assertThat(newCryptoMovementMock.name, isNot(name))
        assertThat(viewModel.isLoading.getOrAwaitValue(delayForLoading), `is`(false))
    }

    @Test
    fun `try to update a crypto movement and handler its error case`() {
        mainCoroutineRule.pauseDispatcher()

        val name = "Testing"

        val crypto = newCryptoMovementMock.copy(
            id = 1L,
            name = name
        )

        viewModel.attachCryptoMovementForUpdate(crypto)

        coEvery { movementRepository.updateCrypto(any()) } returns ResultEvent.Error(exceptionMock)

        viewModel.saveCrypto()

        assertThat(viewModel.isLoading.getOrAwaitValue(), `is`(true))

        mainCoroutineRule.resumeDispatcher()

        val resultEvent = viewModel.updateResultEvent.getOrAwaitValue()

        assertThat(resultEvent.succeeded, `is`(false))
        assertThat((resultEvent as ResultEvent.Error).exception, `is`(exceptionMock))
        assertThat(crypto.name, isNot(name))
        assertThat(viewModel.isLoading.getOrAwaitValue(delayForLoading), `is`(false))
    }

    /**
     * Load names
     * */

    @Test
    fun `load crypto names using a valid word and getting the names`() {
        val word = "ABC"
        val names = arrayListOf("A", "AA", "B", "AB", "CC", "CB")

        every { movementRepository.fetchCryptoNames(any()) } returns ResultEvent.Success(names)

        viewModel.fetchCryptos(word)

        val result = viewModel.cryptoNamesResultEvent.getOrAwaitValue()

        assertThat(result.succeeded, `is`(true))

        val dataResult = (result as ResultEvent.Success).data
        assertThat(result.data.size, `is`(names.size))

        names.forEachIndexed { index, s ->
            assertThat(s, `is`(dataResult[index]))
        }
    }

    @Test
    fun `load crypto names passing a empty name and getting nothing`() {
        val word = ""
        val names = arrayListOf("A", "AA", "B", "AB", "CC", "CB", "D")

        every { movementRepository.fetchCryptoNames(any()) } returns ResultEvent.Success(names)

        viewModel.fetchCryptos(word)

        val result = viewModel.cryptoNamesResultEvent.getOrAwaitValue()

        assertThat(result.succeeded, `is`(true))
        assertThat((result as ResultEvent.Success).data.size, `is`(0))
    }

    @Test
    fun `try loading crypto names but getting nothing`() {
        val word = "AB"

        every { movementRepository.fetchCryptoNames(any()) } returns ResultEvent.Error(exceptionMock)

        viewModel.fetchCryptos(word)

        val result = viewModel.cryptoNamesResultEvent.getOrAwaitValue()

        assertThat(result.succeeded, `is`(false))
        assertThat((result as ResultEvent.Error).exception, `is`(exceptionMock))
    }

    /**
     * Fetch last crypto
     * */

    @Test
    fun `loading the last crypto and return one`() {
        every { movementRepository.fetchLastCrypto(any()) } returns ResultEvent.Success(
            newCryptoMovementMock
        )

        viewModel.fetchLastCrypto("")

        val result = viewModel.lastTradeResultEvent.getOrAwaitValue()

        assertThat(result.succeeded, `is`(true))
        assertThat((result as ResultEvent.Success).data, `is`(newCryptoMovementMock))
    }

    @Test
    fun `try loading the last crypto and return an error`() {
        every { movementRepository.fetchLastCrypto(any()) } returns ResultEvent.Error(exceptionMock)

        viewModel.fetchLastCrypto("")

        val result = viewModel.lastTradeResultEvent.getOrAwaitValue()

        assertThat(result.succeeded, `is`(false))
        assertThat((result as ResultEvent.Error).exception, `is`(exceptionMock))
    }

    /**
     * Retrieve date
     * */

    @Test
    fun `retrieve the data from a attached trade`() {
        viewModel.attachCryptoMovementForUpdate(newCryptoMovementMock)

        viewModel.retrieveDate()

        val date = viewModel.dateRetrieved.getOrAwaitValue()

        assertThat(date, `is`(newCryptoMovementMock.date))
    }

    @Test
    fun `retrieve the data without attach a trade`() {
        viewModel.retrieveDate()

        val date = viewModel.dateRetrieved.getOrAwaitValue()

        val today = Calendar.getInstance()
        assertThat(date.get(Calendar.DAY_OF_MONTH), `is`(today.get(Calendar.DAY_OF_MONTH)))
        assertThat(date.get(Calendar.MONTH), `is`(today.get(Calendar.MONTH)))
        assertThat(date.get(Calendar.YEAR), `is`(today.get(Calendar.YEAR)))
    }
}
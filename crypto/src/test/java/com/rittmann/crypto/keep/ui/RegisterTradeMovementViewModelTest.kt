package com.rittmann.crypto.keep.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.lifecycle.DefaultDispatcherProvider
import com.rittmann.crypto.keep.domain.RegisterCryptoMovementRepository
import com.rittmann.common_test.mock.registeredCryptoMovementMock
import com.rittmann.common_test.MainCoroutineRule
import com.rittmann.common_test.getOrAwaitValue
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.datasource.result.succeeded
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.greaterThan
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

    @Before
    fun setupViewModel() {
        MockKAnnotations.init(this)

        viewModel = RegisterCryptoMovementViewModel(
            movementRepository,
            DefaultDispatcherProvider()
        )
    }

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

        assertThat(viewModel.isLoading.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun `update a crypto movement and handler its success case`() {
        viewModel.attachCryptoMovementForUpdate(TradeMovement(id = 1L))

        mainCoroutineRule.pauseDispatcher()

        coEvery { movementRepository.updateCrypto(any()) } returns ResultEvent.Success(1)

        viewModel.saveCrypto()

        assertThat(viewModel.isLoading.getOrAwaitValue(), `is`(true))

        mainCoroutineRule.resumeDispatcher()

        val resultEvent = viewModel.updateResultEvent.getOrAwaitValue()

        assertThat(resultEvent.succeeded, `is`(true))
        assertThat((resultEvent as ResultEvent.Success).data, `is`(1))
        assertThat(viewModel.isLoading.getOrAwaitValue(), `is`(false))
    }
}
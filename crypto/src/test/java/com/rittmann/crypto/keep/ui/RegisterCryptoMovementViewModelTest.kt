package com.rittmann.crypto.keep.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rittmann.common.lifecycle.DefaultDispatcherProvider
import com.rittmann.crypto.keep.domain.RegisterCryptoMovementRepository
import com.rittmann.common_test.mock.registeredCryptoMovementMock
import com.rittmann.common_test.MainCoroutineRule
import com.rittmann.common_test.getOrAwaitValue
import com.rittmann.datasource.result.ResultEvent
import com.rittmann.datasource.result.succeeded
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
class RegisterCryptoMovementViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var movementViewModel: RegisterCryptoMovementViewModel

    @MockK
    private lateinit var movementRepository: RegisterCryptoMovementRepository

    @Before
    fun setupViewModel() {
        MockKAnnotations.init(this)

        movementViewModel = RegisterCryptoMovementViewModel(
            movementRepository,
            DefaultDispatcherProvider()
        )
    }

    @Test
    fun `register a new crypto currency and handle the success case`() {
        mainCoroutineRule.pauseDispatcher()

        coEvery { movementRepository.registerCrypto(any()) } returns ResultEvent.Success(registeredCryptoMovementMock)

        movementViewModel.registerCrypto()

        assertThat(movementViewModel.isLoading.getOrAwaitValue(), `is`(true))

        mainCoroutineRule.resumeDispatcher()

        val resultEvent = movementViewModel.registerResultEvent.getOrAwaitValue()

        assertThat(resultEvent.succeeded, `is`(true))

        resultEvent as ResultEvent.Success
        resultEvent.data.also { result ->
            assertThat(result.id, greaterThan(0L))
            assertThat(result.id, `is`(registeredCryptoMovementMock.id))
            assertThat(result.date, `is`(registeredCryptoMovementMock.date))
            assertThat(result.name, `is`(registeredCryptoMovementMock.name))
            assertThat(result.boughtAmount, `is`(registeredCryptoMovementMock.boughtAmount))
            assertThat(result.totalValue, `is`(registeredCryptoMovementMock.totalValue))
            assertThat(result.currentValue, `is`(registeredCryptoMovementMock.currentValue))
            assertThat(result.tax, `is`(registeredCryptoMovementMock.tax))
        }

        assertThat(movementViewModel.isLoading.getOrAwaitValue(), `is`(false))
    }
}
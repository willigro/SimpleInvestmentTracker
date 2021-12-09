package com.rittmann.crypto.keep.ui;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rittmann.crypto.keep.domain.RegisterCryptoMovementRepository
import com.rittmann.crypto.mock.registeredCryptoMovement
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
            movementRepository
        )
    }

    @Test
    fun `register a new crypto currency and handle the success case`() {
        mainCoroutineRule.pauseDispatcher()

        coEvery { movementRepository.registerCrypto(any()) } returns ResultEvent.Success(registeredCryptoMovement)

        movementViewModel.registerCrypto()

        assertThat(movementViewModel.isLoading.getOrAwaitValue(), `is`(true))

        mainCoroutineRule.resumeDispatcher()

        val resultEvent = movementViewModel.registerResultEvent.getOrAwaitValue()

        assertThat(resultEvent.succeeded, `is`(true))

        resultEvent as ResultEvent.Success
        resultEvent.data.also { result ->
            assertThat(result.id, greaterThan(0L))
            assertThat(result.id, `is`(registeredCryptoMovement.id))
            assertThat(result.date, `is`(registeredCryptoMovement.date))
            assertThat(result.name, `is`(registeredCryptoMovement.name))
            assertThat(result.boughtAmount, `is`(registeredCryptoMovement.boughtAmount))
            assertThat(result.totalValue, `is`(registeredCryptoMovement.totalValue))
            assertThat(result.currentValue, `is`(registeredCryptoMovement.currentValue))
            assertThat(result.tax, `is`(registeredCryptoMovement.tax))
        }

        assertThat(movementViewModel.isLoading.getOrAwaitValue(), `is`(false))
    }
}
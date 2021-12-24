package com.rittmann.crypto.list.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rittmann.common.lifecycle.DefaultDispatcherProvider
import com.rittmann.common_test.MainCoroutineRule
import com.rittmann.common_test.getOrAwaitValue
import com.rittmann.crypto.list.domain.ListCryptoMovementsRepository
import com.rittmann.common_test.mock.listCryptoMovementMock
import com.rittmann.datasource.result.ResultEvent
import com.rittmann.datasource.result.succeeded
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ListCryptoMovementsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ListCryptoMovementsViewModel

    @MockK
    private lateinit var repository: ListCryptoMovementsRepository

    @Before
    fun setupViewModel() {
        MockKAnnotations.init(this)

        viewModel = ListCryptoMovementsViewModel(
            repository,
            DefaultDispatcherProvider()
        )
    }

    @Test
    fun `load all the crypto movements with success`() {
        coEvery { repository.getAll() } returns ResultEvent.Success(listCryptoMovementMock)

        viewModel.fetchAllCryptoMovements()

        val result = viewModel.cryptoMovementsList.getOrAwaitValue()

        assertThat(result.succeeded, `is`(true))

        val list = (result as ResultEvent.Success).data

        assertThat(list.size, greaterThan(0))
        assertThat(list.size, `is`(listCryptoMovementMock.size))
    }
}
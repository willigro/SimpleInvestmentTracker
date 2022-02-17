package com.rittmann.crypto.listmovements.ui

import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.lifecycle.DefaultDispatcherProvider
import com.rittmann.common_test.getOrAwaitValue
import com.rittmann.common_test.mock.currentTotalEarned
import com.rittmann.common_test.mock.currentTotalInvested
import com.rittmann.common_test.mock.listCryptoMovementMock
import com.rittmann.crypto.BaseViewModelTest
import com.rittmann.crypto.listmovements.domain.ListCryptoMovementsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ListTradeMovementsViewModelTest : BaseViewModelTest() {


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

        val result = viewModel.tradeMovementsList.getOrAwaitValue()

        assertThat(result.succeeded, `is`(true))

        val list = (result as ResultEvent.Success).data

        assertThat(list.size, greaterThan(0))
        assertThat(list.size, `is`(listCryptoMovementMock.size))

        assertThat(
            viewModel.totalValueEarned.getOrAwaitValue(),
            `is`(currentTotalEarned.toString())
        )
        assertThat(
            viewModel.totalValueInvested.getOrAwaitValue(),
            `is`(currentTotalInvested.toString())
        )
    }
}
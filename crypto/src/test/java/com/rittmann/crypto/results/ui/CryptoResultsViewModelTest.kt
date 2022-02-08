package com.rittmann.crypto.results.ui

import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.datasource.result.succeeded
import com.rittmann.common_test.getOrAwaitValue
import com.rittmann.common_test.mock.exceptionMock
import com.rittmann.crypto.BaseViewModelTest
import com.rittmann.crypto.results.domain.CryptoResultsRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CryptoResultsViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: CryptoResultsViewModel

    @MockK
    private lateinit var repository: CryptoResultsRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = CryptoResultsViewModel(
            repository
        )
    }

    @Test
    fun `load all movements from BTC and calculate its results`() {
        val name = "BTC"

        val crypto = TradeMovement(
            name = name,
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

        every { repository.fetchCryptos(name) } returns ResultEvent.Success(list)

        viewModel.fetchCryptos(name)

        val result = viewModel.cryptosResult.getOrAwaitValue()

        assertThat(result.succeeded, `is`(true))

        with((result as ResultEvent.Success).data) {
            assertThat(this.size, `is`(list.size))
        }

        with(viewModel.cryptoResultViewBinding) {
            assertThat(title.getOrAwaitValue(), `is`(name))
            assertThat(totalOnHand.getOrAwaitValue(), `is`("-25.0"))
            assertThat(totalEarned.getOrAwaitValue(), `is`("15.0"))
            assertThat(totalInvested.getOrAwaitValue(), `is`("40.0"))
            assertThat(totalBoughtAmount.getOrAwaitValue(), `is`("25.0"))
            assertThat(totalSoldAmount.getOrAwaitValue(), `is`("5.0"))
            assertThat(totalOnHandAmount.getOrAwaitValue(), `is`("20.0"))
            assertThat(totalTaxPaid.getOrAwaitValue(), `is`("3.0"))
        }
    }

    @Test
    fun `load an unregistered currency XYZ`() {
        val name = "XYZ"

        every { repository.fetchCryptos(name) } returns ResultEvent.Success(emptyList())

        viewModel.fetchCryptos(name)

        val result = viewModel.cryptosResult.getOrAwaitValue()

        assertThat(result.succeeded, `is`(true))

        with((result as ResultEvent.Success).data) {
            assertThat(this.size, `is`(0))
        }

        with(viewModel.cryptoResultViewBinding) {
            assertThat(title.getOrAwaitValue(), `is`(name))
            assertThat(totalOnHand.getOrAwaitValue(), `is`("0.0"))
            assertThat(totalEarned.getOrAwaitValue(), `is`("0.0"))
            assertThat(totalInvested.getOrAwaitValue(), `is`("0.0"))
            assertThat(totalBoughtAmount.getOrAwaitValue(), `is`("0.0"))
            assertThat(totalSoldAmount.getOrAwaitValue(), `is`("0.0"))
            assertThat(totalOnHandAmount.getOrAwaitValue(), `is`("0.0"))
            assertThat(totalTaxPaid.getOrAwaitValue(), `is`("0.0"))
        }
    }


    @Test
    fun `load an some currency XYZ and returns an error`() {
        val name = "XYZ"

        every { repository.fetchCryptos(name) } returns ResultEvent.Error(exceptionMock)

        viewModel.fetchCryptos(name)

        val result = viewModel.cryptosResult.getOrAwaitValue()

        assertThat(result.succeeded, `is`(false))

        with((result as ResultEvent.Error).exception) {
            assertThat(this, `is`(exceptionMock))
        }

        with(viewModel.cryptoResultViewBinding) {
            assertThat(title.getOrAwaitValue(), `is`(name))
            assertThat(totalOnHand.getOrAwaitValue(), `is`("0.0"))
            assertThat(totalEarned.getOrAwaitValue(), `is`("0.0"))
            assertThat(totalInvested.getOrAwaitValue(), `is`("0.0"))
            assertThat(totalBoughtAmount.getOrAwaitValue(), `is`("0.0"))
            assertThat(totalSoldAmount.getOrAwaitValue(), `is`("0.0"))
            assertThat(totalOnHandAmount.getOrAwaitValue(), `is`("0.0"))
            assertThat(totalTaxPaid.getOrAwaitValue(), `is`("0.0"))
        }
    }
}
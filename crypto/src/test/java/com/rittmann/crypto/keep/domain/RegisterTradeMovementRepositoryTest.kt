package com.rittmann.crypto.keep.domain

import com.rittmann.common_test.mock.newCryptoMovementMock
import com.rittmann.common.datasource.dao.interfaces.TradeDao
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.datasource.result.succeeded
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.greaterThan
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RegisterTradeMovementRepositoryTest {

    private lateinit var registerCryptoMovementRepository: RegisterCryptoMovementRepository

    @MockK
    private lateinit var tradeDao: TradeDao

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        registerCryptoMovementRepository = RegisterCryptoMovementRepositoryImpl(tradeDao)
    }

    @Test
    fun `register a new crypto`() = runBlockingTest {
        coEvery { tradeDao.insert(newCryptoMovementMock) } returns 1L

        val resultEvent = registerCryptoMovementRepository.registerCrypto(newCryptoMovementMock)

        assertThat(resultEvent.succeeded, `is`(true))
        (resultEvent as ResultEvent.Success).data.also { result ->
            assertThat(result.id, greaterThan(0L))
            assertThat(result.id, `is`(newCryptoMovementMock.id))
            assertThat(result.date, `is`(newCryptoMovementMock.date))
            assertThat(result.name, `is`(newCryptoMovementMock.name))
            assertThat(result.operatedAmount, `is`(newCryptoMovementMock.operatedAmount))
            assertThat(result.totalValue, `is`(newCryptoMovementMock.totalValue))
            assertThat(result.currentValue, `is`(newCryptoMovementMock.currentValue))
            assertThat(result.tax, `is`(newCryptoMovementMock.tax))
        }
    }
}
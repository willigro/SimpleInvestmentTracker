package com.rittmann.crypto.listmovements.domain

import com.rittmann.common_test.mock.exceptionMock
import com.rittmann.common_test.mock.listCryptoMovementMock
import com.rittmann.common.datasource.dao.interfaces.TradeDao
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.datasource.result.succeeded
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import java.lang.Exception
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.instanceOf
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ListTradeMovementsRepositoryTest {

    private lateinit var listCryptoMovementsRepository: ListCryptoMovementsRepository

    @MockK
    private lateinit var tradeDao: TradeDao

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        listCryptoMovementsRepository = ListCryptoMovementsRepositoryImpl(
            tradeDao
        )
    }

    @Test
    fun `select all crypto movements and return success`() = runBlockingTest {
        coEvery { tradeDao.selectAll() } returns listCryptoMovementMock

        val result = listCryptoMovementsRepository.getAll()

        assertThat(result.succeeded, `is`(true))

        (result as ResultEvent.Success).data.apply {
            assertThat(size, `is`(listCryptoMovementMock.size))
        }
    }

    @Test
    fun `select all returns an exception`() = runBlockingTest {
        coEvery { tradeDao.selectAll() } throws exceptionMock

        val result = listCryptoMovementsRepository.getAll()

        assertThat(result.succeeded, `is`(false))

        (result as ResultEvent.Error).exception.apply {
            assertThat(this, instanceOf(Exception::class.java))
        }
    }
}
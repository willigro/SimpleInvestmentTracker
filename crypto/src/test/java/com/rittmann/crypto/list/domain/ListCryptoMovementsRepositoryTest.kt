package com.rittmann.crypto.list.domain

import com.rittmann.common_test.mock.exceptionMock
import com.rittmann.common_test.mock.listCryptoMovementMock
import com.rittmann.datasource.dao.interfaces.CryptoDao
import com.rittmann.datasource.result.ResultEvent
import com.rittmann.datasource.result.succeeded
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
class ListCryptoMovementsRepositoryTest {

    private lateinit var listCryptoMovementsRepository: ListCryptoMovementsRepository

    @MockK
    private lateinit var cryptoDao: CryptoDao

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        listCryptoMovementsRepository = ListCryptoMovementsRepositoryImpl(
            cryptoDao
        )
    }

    @Test
    fun `select all crypto movements and return success`() = runBlockingTest {
        coEvery { cryptoDao.selectAll() } returns listCryptoMovementMock

        val result = listCryptoMovementsRepository.getAll()

        assertThat(result.succeeded, `is`(true))

        (result as ResultEvent.Success).data.apply {
            assertThat(size, `is`(listCryptoMovementMock.size))
        }
    }

    @Test
    fun `select all returns an exception`() = runBlockingTest {
        coEvery { cryptoDao.selectAll() } throws exceptionMock

        val result = listCryptoMovementsRepository.getAll()

        assertThat(result.succeeded, `is`(false))

        (result as ResultEvent.Error).exception.apply {
            assertThat(this, instanceOf(Exception::class.java))
        }
    }
}
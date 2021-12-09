package com.rittmann.crypto.keep.domain

import com.rittmann.crypto.mock.newCryptoMovement
import com.rittmann.datasource.dao.interfaces.CryptoDao
import com.rittmann.datasource.result.ResultEvent
import com.rittmann.datasource.result.succeeded
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
class RegisterCryptoMovementRepositoryImplTest {

    private lateinit var registerCryptoMovementRepository: RegisterCryptoMovementRepository

    @MockK
    private lateinit var cryptoDao: CryptoDao

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        registerCryptoMovementRepository = RegisterCryptoMovementRepositoryImpl(cryptoDao)
    }

    @Test
    fun `register a new crypto`() = runBlockingTest {
        coEvery { cryptoDao.insert(newCryptoMovement) } returns 1L

        val resultEvent = registerCryptoMovementRepository.registerCrypto(newCryptoMovement)

        assertThat(resultEvent.succeeded, `is`(true))
        (resultEvent as ResultEvent.Success).data.also { result ->
            assertThat(result.id, greaterThan(0L))
            assertThat(result.id, `is`(newCryptoMovement.id))
            assertThat(result.date, `is`(newCryptoMovement.date))
            assertThat(result.name, `is`(newCryptoMovement.name))
            assertThat(result.boughtAmount, `is`(newCryptoMovement.boughtAmount))
            assertThat(result.totalValue, `is`(newCryptoMovement.totalValue))
            assertThat(result.currentValue, `is`(newCryptoMovement.currentValue))
            assertThat(result.tax, `is`(newCryptoMovement.tax))
        }
    }
}
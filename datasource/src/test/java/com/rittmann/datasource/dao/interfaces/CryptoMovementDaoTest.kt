package com.rittmann.datasource.dao.interfaces

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rittmann.datasource.basic.CryptoOperationType
import com.rittmann.datasource.dao.config.AppDatabase
import com.rittmann.datasource.dao.mock.newCrypto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.nullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CryptoMovementDaoTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase

    private lateinit var dao: CryptoDao

    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.cryptoDao()
    }

    @After
    fun clearDb() {
        dao.deleteAll()
        database.close()
    }

    @Test
    fun `insert a new crypto and check if it was correctly registered`() {
        val resultId = dao.insert(newCrypto)

        assertThat(resultId, greaterThan(0L))

        val result = dao.selectById(resultId)

        assertThat(result!!.id, greaterThan(0L))
        assertThat(result.id, `is`(resultId))
        assertThat(result.date, `is`(newCrypto.date))
        assertThat(result.name, `is`(newCrypto.name))
        assertThat(result.boughtAmount, `is`(newCrypto.boughtAmount))
        assertThat(result.totalValue, `is`(newCrypto.totalValue))
        assertThat(result.currentValue, `is`(newCrypto.currentValue))
        assertThat(result.tax, `is`(newCrypto.tax))
    }

    @Test
    fun `insert a new crypto, delete it and check if it was correctly deleted`() {
        val resultId = dao.insert(newCrypto)

        newCrypto.id = resultId

        assertThat(resultId, greaterThan(0L))

        val deletedRows = dao.delete(newCrypto)

        assertThat(deletedRows, `is`(1))

        val result = dao.selectById(resultId)

        assertThat(result, nullValue())
    }

    @Test
    fun `insert a list of crypto, delete all and check if the table is empty`() {
        for (i in 1..10) {
            dao.insert(newCrypto)
        }

        val allCryptos = dao.selectAll()

        assertThat(allCryptos.size, greaterThan(0))

        val deletedRows = dao.deleteAll()

        assertThat(deletedRows, `is`(allCryptos.size))

        val allCryptosAgain = dao.selectAll()

        assertThat(allCryptosAgain.size, `is`(0))
    }

    @Test
    fun `insert a crypto update it and check if the value was right updated`() {
        val resultId = dao.insert(newCrypto)

        val cryptoToUpdate = newCrypto.copy(
            id = resultId,
            name = "Name updated",
            date = "11/11/1111",
            type = CryptoOperationType.SELL,
            boughtAmount = 5,
            currentValue = 50.0,
            totalValue = 250.0,
            tax = 5.0
        )

        val updateRow = dao.update(cryptoToUpdate)

        assertThat(updateRow, `is`(1))

        val updatedCrypto = dao.selectById(resultId)

        assertThat(updatedCrypto!!.id, greaterThan(0L))
        assertThat(updatedCrypto.id, `is`(resultId))
        assertThat(updatedCrypto.date, `is`(cryptoToUpdate.date))
        assertThat(updatedCrypto.name, `is`(cryptoToUpdate.name))
        assertThat(updatedCrypto.boughtAmount, `is`(cryptoToUpdate.boughtAmount))
        assertThat(updatedCrypto.totalValue, `is`(cryptoToUpdate.totalValue))
        assertThat(updatedCrypto.currentValue, `is`(cryptoToUpdate.currentValue))
        assertThat(updatedCrypto.tax, `is`(cryptoToUpdate.tax))
    }
}
package com.rittmann.crypto.listmovements.domain

import androidx.sqlite.db.SimpleSQLiteQuery
import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.dao.config.QueryDAO
import com.rittmann.common.datasource.dao.config.TableCryptoMovement
import com.rittmann.common.datasource.dao.config.groupByThat
import com.rittmann.common.datasource.dao.config.orderBy
import com.rittmann.common.datasource.dao.config.selectAll
import com.rittmann.common.datasource.dao.interfaces.CryptoDao
import com.rittmann.common.datasource.result.ResultEvent
import javax.inject.Inject

interface ListCryptoMovementsRepository {
    suspend fun getAll(): ResultEvent<List<CryptoMovement>>
    suspend fun delete(cryptoMovement: CryptoMovement): ResultEvent<Int>
}

class ListCryptoMovementsRepositoryImpl @Inject constructor(
    private val cryptoDao: CryptoDao
) : ListCryptoMovementsRepository {

    override suspend fun getAll(): ResultEvent<List<CryptoMovement>> {
        return try {
            val query = SimpleSQLiteQuery(
                TableCryptoMovement.TABLE.selectAll() +
                        TableCryptoMovement.DATE.groupByThat() +
                        TableCryptoMovement.DATE.orderBy(QueryDAO.DESC)
            )
            ResultEvent.Success(cryptoDao.selectAll(query))
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }

    override suspend fun delete(cryptoMovement: CryptoMovement): ResultEvent<Int> {
        return try {
            ResultEvent.Success(cryptoDao.delete(cryptoMovement))
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }
}


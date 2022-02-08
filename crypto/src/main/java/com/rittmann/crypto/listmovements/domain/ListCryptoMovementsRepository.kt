package com.rittmann.crypto.listmovements.domain

import androidx.sqlite.db.SimpleSQLiteQuery
import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.dao.config.QueryDAO
import com.rittmann.common.datasource.dao.config.TableTradeMovement
import com.rittmann.common.datasource.dao.config.groupByThat
import com.rittmann.common.datasource.dao.config.orderBy
import com.rittmann.common.datasource.dao.config.selectAll
import com.rittmann.common.datasource.dao.interfaces.TradeDao
import com.rittmann.common.datasource.result.ResultEvent
import javax.inject.Inject

interface ListCryptoMovementsRepository {
    suspend fun getAll(): ResultEvent<List<TradeMovement>>
    suspend fun delete(tradeMovement: TradeMovement): ResultEvent<Int>
}

class ListCryptoMovementsRepositoryImpl @Inject constructor(
    private val tradeDao: TradeDao
) : ListCryptoMovementsRepository {

    override suspend fun getAll(): ResultEvent<List<TradeMovement>> {
        return try {
            val query = SimpleSQLiteQuery(
                TableTradeMovement.TABLE.selectAll() +
                        TableTradeMovement.DATE.groupByThat() +
                        TableTradeMovement.DATE.orderBy(QueryDAO.DESC)
            )
            ResultEvent.Success(tradeDao.selectAll(query))
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }

    override suspend fun delete(tradeMovement: TradeMovement): ResultEvent<Int> {
        return try {
            ResultEvent.Success(tradeDao.delete(tradeMovement))
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }
}


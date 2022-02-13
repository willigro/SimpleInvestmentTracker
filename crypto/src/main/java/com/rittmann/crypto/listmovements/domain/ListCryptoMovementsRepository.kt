package com.rittmann.crypto.listmovements.domain

import androidx.sqlite.db.SimpleSQLiteQuery
import com.rittmann.androidtools.log.log
import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.dao.config.QueryDAO
import com.rittmann.common.datasource.dao.config.TableTradeMovement
import com.rittmann.common.datasource.dao.config.groupByThat
import com.rittmann.common.datasource.dao.config.limitAndOffset
import com.rittmann.common.datasource.dao.config.orderBy
import com.rittmann.common.datasource.dao.config.selectAll
import com.rittmann.common.datasource.dao.interfaces.TradeDao
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.utils.pagination.PageInfo
import javax.inject.Inject

interface ListCryptoMovementsRepository {
    suspend fun getAll(pageInfo: PageInfo<TradeMovement>): ResultEvent<List<TradeMovement>>
    suspend fun delete(tradeMovement: TradeMovement): ResultEvent<Int>
}

class ListCryptoMovementsRepositoryImpl @Inject constructor(
    private val tradeDao: TradeDao
) : ListCryptoMovementsRepository {

    override suspend fun getAll(pageInfo: PageInfo<TradeMovement>): ResultEvent<List<TradeMovement>> {
        return try {
            val strQuery = TableTradeMovement.TABLE.selectAll() +
//                    TableTradeMovement.DATE.groupByThat() +
                    TableTradeMovement.DATE.orderBy(QueryDAO.DESC)
                        .limitAndOffset(pageInfo.size, pageInfo.page)
            val query = SimpleSQLiteQuery(
                strQuery.apply {
                    log()
                }
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


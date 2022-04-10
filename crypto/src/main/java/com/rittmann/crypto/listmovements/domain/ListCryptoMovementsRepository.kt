package com.rittmann.crypto.listmovements.domain

import androidx.sqlite.db.SimpleSQLiteQuery
import com.rittmann.androidtools.log.log
import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.dao.config.QueryDAO
import com.rittmann.common.datasource.dao.config.TableTradeMovement
import com.rittmann.common.datasource.dao.config.limitAndOffset
import com.rittmann.common.datasource.dao.config.orderBy
import com.rittmann.common.datasource.dao.config.selectAll
import com.rittmann.common.datasource.dao.interfaces.TradeDao
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.utils.pagination.PageInfo
import com.rittmann.crypto.listmovements.ui.TradeFilter
import javax.inject.Inject

interface ListCryptoMovementsRepository {
    fun getAll(
        pageInfo: PageInfo<TradeMovement>,
        tradeFilter: TradeFilter
    ): ResultEvent<List<TradeMovement>>

    fun delete(tradeMovement: TradeMovement): ResultEvent<TradeMovement>
    fun fetchTradeNames(): ResultEvent<List<String>>
}

class ListCryptoMovementsRepositoryImpl @Inject constructor(
    private val tradeDao: TradeDao
) : ListCryptoMovementsRepository {

    override fun getAll(
        pageInfo: PageInfo<TradeMovement>,
        tradeFilter: TradeFilter
    ): ResultEvent<List<TradeMovement>> {
        return try {

            val where = if (tradeFilter.nameFilters.isEmpty()) ""
            else {
                var nameIn = " ${TableTradeMovement.NAME} IN ("
                tradeFilter.nameFilters.forEachIndexed { index, s ->
                    if (s.isNotEmpty()) {
                        nameIn += if (index == 0) {
                            "'$s'"
                        } else {
                            " , '$s'"
                        }
                    }
                }
                "$nameIn)"
            }

            val strQuery = TableTradeMovement.TABLE.selectAll(where) +
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

    override fun delete(tradeMovement: TradeMovement): ResultEvent<TradeMovement> {
        return try {
            if (tradeDao.delete(tradeMovement) > 0)
                ResultEvent.Success(tradeMovement)
            else
                ResultEvent.Error(Exception())
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }

    override fun fetchTradeNames(): ResultEvent<List<String>> {
        return try {
            ResultEvent.Success(tradeDao.selectNames())
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }
}


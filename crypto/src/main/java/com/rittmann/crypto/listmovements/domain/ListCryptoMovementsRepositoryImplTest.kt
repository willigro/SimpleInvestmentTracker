package com.rittmann.crypto.listmovements.domain

import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.dao.interfaces.TradeDao
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.utils.pagination.PageInfo
import com.rittmann.crypto.listmovements.ui.TradeFilter
import javax.inject.Inject

class ListCryptoMovementsRepositoryImplTest @Inject constructor(
    private val tradeDao: TradeDao
) : ListCryptoMovementsRepository {

    companion object {
        var returnsError = true
    }

    override suspend fun getAll(pageInfo: PageInfo<TradeMovement>, tradeFilter: TradeFilter): ResultEvent<List<TradeMovement>> {
        return if (returnsError)
            ResultEvent.Error(Exception())
        else
            ResultEvent.Success(tradeDao.selectAll())
    }

    override suspend fun delete(tradeMovement: TradeMovement): ResultEvent<TradeMovement> {
        return ResultEvent.Success(tradeMovement)
    }

    override suspend fun fetchTradeNames(): ResultEvent<List<String>> {
        return ResultEvent.Success(emptyList())
    }
}
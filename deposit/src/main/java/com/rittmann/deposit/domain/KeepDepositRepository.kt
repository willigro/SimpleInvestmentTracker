package com.rittmann.deposit.domain

import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.dao.interfaces.TradeDao
import com.rittmann.common.datasource.result.ResultEvent

interface KeepDepositRepository {
    suspend fun register(tradeMovement: TradeMovement): ResultEvent<TradeMovement>
    suspend fun update(tradeMovement: TradeMovement): ResultEvent<Int>
}

class KeepDepositRepositoryImpl(private val tradeDao: TradeDao) :
    KeepDepositRepository {
    override suspend fun register(tradeMovement: TradeMovement): ResultEvent<TradeMovement> {
        return try {
            tradeDao.insert(tradeMovement).let {
                tradeMovement.id = it
                ResultEvent.Success(tradeMovement)
            }
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }

    override suspend fun update(tradeMovement: TradeMovement): ResultEvent<Int> {
        return try {
            ResultEvent.Success(tradeDao.update(tradeMovement))
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }
}
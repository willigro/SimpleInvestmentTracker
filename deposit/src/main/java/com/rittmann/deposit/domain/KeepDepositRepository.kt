package com.rittmann.deposit.domain

import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.dao.interfaces.TradeDao
import com.rittmann.common.datasource.result.ResultEvent

interface KeepDepositRepository {
    fun register(tradeMovement: TradeMovement): ResultEvent<TradeMovement>
    fun update(tradeMovement: TradeMovement): ResultEvent<TradeMovement>
}

class KeepDepositRepositoryImpl(private val tradeDao: TradeDao) :
    KeepDepositRepository {
    override fun register(tradeMovement: TradeMovement): ResultEvent<TradeMovement> {
        return try {
            tradeDao.insert(tradeMovement).let {
                tradeMovement.id = it
                ResultEvent.Success(tradeMovement)
            }
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }

    override fun update(tradeMovement: TradeMovement): ResultEvent<TradeMovement> {
        return try {
            if (tradeDao.update(tradeMovement) > 0)
                ResultEvent.Success(tradeMovement)
            else
                ResultEvent.Error(java.lang.Exception())
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }
}
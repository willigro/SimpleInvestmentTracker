package com.rittmann.crypto.listcryptos.domain

import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.dao.interfaces.TradeDao
import com.rittmann.common.datasource.result.ResultEvent

interface ListCryptosRepository {
    fun fetchCryptoNames(): ResultEvent<List<String>>
    fun fetchCryptos(): ResultEvent<List<TradeMovement>>
}

class ListCryptosRepositoryImpl(
    private val tradeDao: TradeDao
) : ListCryptosRepository {
    override fun fetchCryptoNames(): ResultEvent<List<String>> {
        return try {
            ResultEvent.Success(tradeDao.selectNames())
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }

    override fun fetchCryptos(): ResultEvent<List<TradeMovement>> {
        return try {
            ResultEvent.Success(tradeDao.selectAll())
        } catch (e: java.lang.Exception) {
            ResultEvent.Error(e)
        }
    }
}
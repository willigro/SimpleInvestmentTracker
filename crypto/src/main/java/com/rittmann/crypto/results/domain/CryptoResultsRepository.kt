package com.rittmann.crypto.results.domain

import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.dao.interfaces.TradeDao
import com.rittmann.common.datasource.result.ResultEvent
import java.lang.Exception

interface CryptoResultsRepository {
    fun fetchCryptos(cryptoName: String): ResultEvent<List<TradeMovement>>
}

class CryptoResultsRepositoryImpl(private val tradeDao: TradeDao): CryptoResultsRepository {

    override fun fetchCryptos(cryptoName: String): ResultEvent<List<TradeMovement>> {
        return try {
            ResultEvent.Success(tradeDao.selectByName(cryptoName))
        }catch (e: Exception){
            ResultEvent.Error(e)
        }
    }
}
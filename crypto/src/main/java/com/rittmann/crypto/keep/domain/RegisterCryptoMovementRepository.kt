package com.rittmann.crypto.keep.domain

import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.dao.interfaces.TradeDao
import com.rittmann.common.datasource.result.ResultEvent

interface RegisterCryptoMovementRepository {

    suspend fun registerCrypto(tradeMovement: TradeMovement): ResultEvent<TradeMovement>
    suspend fun updateCrypto(tradeMovement: TradeMovement): ResultEvent<Int>
    suspend fun fetchCryptoNames(nameLike: String): ResultEvent<List<String>>
    suspend fun fetchLastCrypto(name: String): ResultEvent<TradeMovement>
}

class RegisterCryptoMovementRepositoryImpl(
    private val tradeDao: TradeDao
) : RegisterCryptoMovementRepository {

    override suspend fun registerCrypto(tradeMovement: TradeMovement): ResultEvent<TradeMovement> {
        return try {
            tradeDao.insert(tradeMovement).let {
                tradeMovement.id = it
                ResultEvent.Success(tradeMovement)
            }
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }

    override suspend fun updateCrypto(tradeMovement: TradeMovement): ResultEvent<Int> {
        return try {
            ResultEvent.Success(tradeDao.update(tradeMovement))
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }

    override suspend fun fetchCryptoNames(nameLike: String): ResultEvent<List<String>> {
        return try {
            ResultEvent.Success(tradeDao.selectNamesLike("%$nameLike%"))
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }

    override suspend fun fetchLastCrypto(name: String): ResultEvent<TradeMovement> {
        return try {
            ResultEvent.Success(tradeDao.selectLast(name))
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }
}
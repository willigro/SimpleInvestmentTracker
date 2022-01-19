package com.rittmann.crypto.listmovements.domain

import com.rittmann.common.datasource.basic.CryptoMovement
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
            ResultEvent.Success(cryptoDao.selectAll())
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


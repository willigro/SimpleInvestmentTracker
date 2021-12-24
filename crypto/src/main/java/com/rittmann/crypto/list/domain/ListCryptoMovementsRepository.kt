package com.rittmann.crypto.list.domain

import com.rittmann.datasource.basic.CryptoMovement
import com.rittmann.datasource.dao.interfaces.CryptoDao
import com.rittmann.datasource.result.ResultEvent
import javax.inject.Inject

interface ListCryptoMovementsRepository {
    suspend fun getAll(): ResultEvent<List<CryptoMovement>>
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
}
package com.rittmann.crypto.keep.domain

import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.dao.interfaces.CryptoDao
import com.rittmann.common.datasource.result.ResultEvent

interface RegisterCryptoMovementRepository {

    suspend fun registerCrypto(cryptoMovement: CryptoMovement): ResultEvent<CryptoMovement>
    suspend fun updateCrypto(cryptoMovement: CryptoMovement): ResultEvent<Int>
    suspend fun fetchCryptoNames(nameLike: String): ResultEvent<List<String>>
    suspend fun fetchLastCrypto(name: String): ResultEvent<CryptoMovement>
}

class RegisterCryptoMovementRepositoryImpl(
    private val cryptoDao: CryptoDao
) : RegisterCryptoMovementRepository {

    override suspend fun registerCrypto(cryptoMovement: CryptoMovement): ResultEvent<CryptoMovement> {
        return try {
            cryptoDao.insert(cryptoMovement).let {
                cryptoMovement.id = it
                ResultEvent.Success(cryptoMovement)
            }
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }

    override suspend fun updateCrypto(cryptoMovement: CryptoMovement): ResultEvent<Int> {
        return try {
            ResultEvent.Success(cryptoDao.update(cryptoMovement))
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }

    override suspend fun fetchCryptoNames(nameLike: String): ResultEvent<List<String>> {
        return try {
            ResultEvent.Success(cryptoDao.selectNamesLike("%$nameLike%"))
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }

    override suspend fun fetchLastCrypto(name: String): ResultEvent<CryptoMovement> {
        return try {
            ResultEvent.Success(cryptoDao.selectLast(name))
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }
}
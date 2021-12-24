package com.rittmann.crypto.keep.domain

import com.rittmann.datasource.basic.CryptoMovement
import com.rittmann.datasource.dao.interfaces.CryptoDao
import com.rittmann.datasource.result.ResultEvent

interface RegisterCryptoMovementRepository {

    suspend fun registerCrypto(cryptoMovement: CryptoMovement): ResultEvent<CryptoMovement>
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
}
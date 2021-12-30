package com.rittmann.crypto.results.domain

import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.dao.interfaces.CryptoDao
import com.rittmann.common.datasource.result.ResultEvent
import java.lang.Exception

interface CryptoResultsRepository {
    fun fetchCryptos(cryptoName: String): ResultEvent<List<CryptoMovement>>
}

class CryptoResultsRepositoryImpl(private val cryptoDao: CryptoDao): CryptoResultsRepository {

    override fun fetchCryptos(cryptoName: String): ResultEvent<List<CryptoMovement>> {
        return try {
            ResultEvent.Success(cryptoDao.selectByName(cryptoName))
        }catch (e: Exception){
            ResultEvent.Error(e)
        }
    }
}
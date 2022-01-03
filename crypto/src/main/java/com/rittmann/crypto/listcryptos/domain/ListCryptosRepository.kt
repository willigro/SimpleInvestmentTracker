package com.rittmann.crypto.listcryptos.domain

import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.dao.interfaces.CryptoDao
import com.rittmann.common.datasource.result.ResultEvent

interface ListCryptosRepository {
    fun getAll(): ResultEvent<List<String>>
    fun fetchCryptos(): ResultEvent<List<CryptoMovement>>
}

class ListCryptosRepositoryImpl(
    private val cryptoDao: CryptoDao
) : ListCryptosRepository {
    override fun getAll(): ResultEvent<List<String>> {
        return try {
            ResultEvent.Success(
                arrayListOf(
                    "BTC"
                )
            )
        } catch (e: Exception) {
            ResultEvent.Error(e)
        }
    }

    override fun fetchCryptos(): ResultEvent<List<CryptoMovement>> {
        return try {
            ResultEvent.Success(cryptoDao.selectAll())
        } catch (e: java.lang.Exception) {
            ResultEvent.Error(e)
        }
    }
}
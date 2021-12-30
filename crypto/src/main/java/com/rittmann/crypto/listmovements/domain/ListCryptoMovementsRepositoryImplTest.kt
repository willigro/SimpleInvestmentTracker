package com.rittmann.crypto.listmovements.domain

import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.dao.interfaces.CryptoDao
import com.rittmann.common.datasource.result.ResultEvent
import javax.inject.Inject

class ListCryptoMovementsRepositoryImplTest @Inject constructor(
    private val cryptoDao: CryptoDao
) : ListCryptoMovementsRepository {

    companion object {
        var returnsError = true
    }

    override suspend fun getAll(): ResultEvent<List<CryptoMovement>> {
        return if (returnsError)
            ResultEvent.Error(Exception())
        else
            ResultEvent.Success(cryptoDao.selectAll())
    }
}
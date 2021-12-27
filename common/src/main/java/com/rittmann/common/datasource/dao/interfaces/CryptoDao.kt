package com.rittmann.common.datasource.dao.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.dao.config.TableCryptoMovement

@Dao
interface CryptoDao {
//    @RawQuery(observedEntities = [CashFlow::class])
//    fun getAll(query: SupportSQLiteQuery): List<CashFlow>

    @Query("SELECT * FROM ${TableCryptoMovement.TABLE}")
    fun selectAll(): List<CryptoMovement>

    @Query("SELECT * FROM ${TableCryptoMovement.TABLE} WHERE ${TableCryptoMovement.ID} = :id")
    fun selectById(id: Long): CryptoMovement?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cryptoMovement: CryptoMovement): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cryptoMovement: List<CryptoMovement>)

    @Delete
    fun delete(cryptoMovement: CryptoMovement): Int

    @Update
    fun update(cryptoMovement: CryptoMovement): Int

    @Query("DELETE FROM ${TableCryptoMovement.TABLE}")
    fun deleteAll(): Int
}

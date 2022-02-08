package com.rittmann.common.datasource.dao.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.dao.config.TableTradeMovement

@Dao
interface TradeDao {
    @RawQuery(observedEntities = [TradeMovement::class])
    fun selectAll(query: SupportSQLiteQuery): List<TradeMovement>

    @Query("SELECT * FROM ${TableTradeMovement.TABLE}")
    fun selectAll(): List<TradeMovement>

    @Query("SELECT * FROM ${TableTradeMovement.TABLE} WHERE ${TableTradeMovement.NAME} = :name COLLATE NOCASE ORDER BY ${TableTradeMovement.ID} DESC LIMIT 1")
    fun selectLast(name: String): TradeMovement

    @Query("SELECT * FROM ${TableTradeMovement.TABLE} WHERE ${TableTradeMovement.ID} = :id")
    fun selectById(id: Long): TradeMovement?

    @Query("SELECT * FROM ${TableTradeMovement.TABLE} WHERE ${TableTradeMovement.NAME} = :name COLLATE NOCASE")
    fun selectByName(name: String): List<TradeMovement>

    @Query("SELECT DISTINCT ${TableTradeMovement.NAME} FROM ${TableTradeMovement.TABLE} WHERE ${TableTradeMovement.NAME} LIKE :name")
    fun selectNamesLike(name: String): List<String>

    @Query("SELECT DISTINCT ${TableTradeMovement.NAME} FROM ${TableTradeMovement.TABLE}")
    fun selectNames(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tradeMovement: TradeMovement): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tradeMovement: List<TradeMovement>)

    @Delete
    fun delete(tradeMovement: TradeMovement): Int

    @Update
    fun update(tradeMovement: TradeMovement): Int

    @Query("DELETE FROM ${TableTradeMovement.TABLE}")
    fun deleteAll(): Int
}

package com.rittmann.common.datasource.dao.config

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rittmann.common.BuildConfig
import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.dao.interfaces.TradeDao

@Database(
    entities = [TradeMovement::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cryptoDao(): TradeDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(application: Application): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            application.applicationContext,
                            AppDatabase::class.java, BuildConfig.BASE_NAME
                        ).build()
                    }
                }
            }
            return INSTANCE
        }
    }
}
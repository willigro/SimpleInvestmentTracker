package com.rittmann.datasource.dao.config

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rittmann.datasource.BuildConfig
import com.rittmann.datasource.basic.CryptoMovement
import com.rittmann.datasource.dao.interfaces.CryptoDao

@Database(
    entities = [CryptoMovement::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cryptoDao(): CryptoDao

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
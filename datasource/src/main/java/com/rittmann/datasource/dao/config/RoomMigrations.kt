//package com.rgames.meusgastos.data.dao.room.config
//
//import androidx.room.migration.Migration
//import androidx.sqlite.db.SupportSQLiteDatabase
//import com.rittmann.datasource.dao.config.TableCashFlow
//import com.rittmann.datasource.dao.config.TableCashFlowPattern
//import com.rittmann.datasource.dao.config.TableCashFlowPatternTag
//import com.rittmann.datasource.dao.config.TableCashFlowTag
//import com.rittmann.datasource.dao.config.TableGoal
//import com.rittmann.datasource.dao.config.TableTag
//
//
//fun migrateToTableFromTable(insert: String, select: String) =
//    "INSERT INTO $insert SELECT * FROM $select;"
//
//// Migration from 1 to 2, Room 2.1.0
//val MIGRATION_1_2 = object : Migration(1, 2) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        try {
//            database.apply {
//                execSQL(TableCashFlow.create())
//                execSQL(TableCashFlowPattern.create())
//
//                val tableCashFlow = com.rgames.meusgastos.data.dao.config.TableCashFlow.TABLE
//                val tableCashFlowPattern =
//                    com.rgames.meusgastos.data.dao.config.TableCashFlowPattern.TABLE
//
//                execSQL(migrateToTableFromTable(TableCashFlow.TABLE, tableCashFlow))
//                execSQL(migrateToTableFromTable(TableCashFlowPattern.TABLE, tableCashFlowPattern))
//
//                execSQL(TableGoal.create())
//                /**
//                 * i wont go delete this base, because i can use it to recover the data
//                 * */
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//}
//
//val MIGRATION_2_3 = object : Migration(2, 3) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        try {
//            database.apply {
//                execSQL(TableTag.create())
//                execSQL(TableCashFlowTag.create())
//                execSQL(TableCashFlowPatternTag.create())
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//}
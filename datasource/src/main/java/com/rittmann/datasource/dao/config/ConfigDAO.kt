package com.rittmann.datasource.dao.config


object TableCryptoMovement {
    const val TABLE = "tb_crypto_movement"
    const val ID = "tb_crypto_movement_id"
    const val DATE = "tb_crypto_movement_date"
    const val NAME = "tb_crypto_movement_name"
    const val TYPE = "tb_crypto_movement_type"
    const val BOUGHT_AMOUNT = "tb_crypto_movement_bought_amount"
    const val CURRENT_VALUE = "tb_crypto_movement_current_value"
    const val TOTAL_VALUE = "tb_crypto_movement_total_value"
    const val TAX = "tb_crypto_movement_tax"

    fun create(): String =
        "$CREATE_TABLE $TABLE " +
                "($ID $INTEGER_PRIMARY," +
                " $NAME TEXT NOT NULL," +
                " $DATE TEXT NOT NULL," +
                " $TYPE TEXT NOT NULL," +
                " $BOUGHT_AMOUNT REAL NOT NULL," +
                " $TOTAL_VALUE REAL NOT NULL," +
                " $TAX REAL NOT NULL," +
                " $CURRENT_VALUE TEXT NOT NULL);"
}

const val CREATE_TABLE = "CREATE TABLE "
const val INTEGER_PRIMARY = " INTEGER PRIMARY KEY AUTOINCREMENT "
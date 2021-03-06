package com.rittmann.common.datasource.dao.config


object TableTradeMovement {
    const val TABLE = "tb_trade_movement"
    const val ID = "tb_trade_movement_id"
    const val DATE = "tb_trade_movement_date"
    const val NAME = "tb_trade_movement_name"
    const val TYPE = "tb_trade_movement_type"
    const val OPERATED_AMOUNT = "tb_trade_movement_operated_amount"
    const val CURRENT_VALUE = "tb_trade_movement_current_value"
    const val CURRENT_VALUE_CURRENCY = "tb_trade_movement_current_value_currency"
    const val TOTAL_VALUE = "tb_trade_movement_total_value"
    const val CONCRETE_TOTAL_VALUE = "tb_trade_movement_concrete_total_value"
    const val TOTAL_VALUE_CURRENCY = "tb_trade_movement_total_value_currency"
    const val TAX = "tb_trade_movement_tax"
    const val TAX_CURRENCY = "tb_trade_movement_tax_currency"
    const val CRYPTO_COIN = "tb_trade_movement_crypto_coin"

    fun create(): String =
        "$CREATE_TABLE $TABLE " +
                "($ID $INTEGER_PRIMARY," +
                " $NAME TEXT NOT NULL," +
                " $DATE TEXT NOT NULL," +
                " $TYPE TEXT NOT NULL," +
                " $OPERATED_AMOUNT TEXT NOT NULL," +
                " $TOTAL_VALUE TEXT NOT NULL," +
                " $TOTAL_VALUE_CURRENCY TEXT NOT NULL," +
                " $CRYPTO_COIN TEXT NOT NULL," +
                " $TAX TEXT NOT NULL," +
                " $TAX_CURRENCY TEXT NOT NULL," +
                " $CURRENT_VALUE_CURRENCY TEXT NOT NULL);"
}

const val CREATE_TABLE = "CREATE TABLE "
const val INTEGER_PRIMARY = " INTEGER PRIMARY KEY AUTOINCREMENT "
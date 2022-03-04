package com.rittmann.investmenttracks

import android.app.Application
import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.dao.config.AppDatabase
import com.rittmann.common.datasource.dao.interfaces.TradeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object MockCurrencies {

    private var dao: TradeDao? = null

    fun mock(application: Application) {
        dao = AppDatabase.getDatabase(application)?.cryptoDao()
        dao?.apply {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    if (selectAll().isEmpty()) {

//                        dao?.insert(
//                            TradeMovement.sell(
//                                name = "Testing",
//                                date = "18/02/2022",
//                                operatedAmount = 1.0,
//                                currentValue = 10000.0,
//                                totalValue = 10000.0,
//                                tax = 0.0,
//                            )
//                        )

                        addBitcoin()
                        addEth()
                        addLtc()
                        addZrx()
                        addAda()
                        addKnc()
                        addDot()
                        addSlp()
                        addMatic()
                        addDeposit()
                        addWithdraw()
                    }
                }
            }
        }
    }

    private fun addBitcoin() {
        val name = "BTC"
        val arr = arrayListOf(
            TradeMovement.buy(
                name = name,
                date = "24/04/2021",
                operatedAmount = 0.00017955,
                currentValue = 278435.2800,
                totalValue = 49.9930,
                concreteTotalValue = 50.0000,
                tax = 0.00000089,
            ),

            TradeMovement.buy(
                name = name,
                date = "24/04/2021",
                operatedAmount = 0.00017963,
                currentValue = 278337.0099,
                totalValue = 49.9976,
                concreteTotalValue = 50.0000,
                tax = 0.00000089,
            ),

            TradeMovement.buy(
                name = name,
                date = "25/04/2021",
                operatedAmount = 0.00089818,
                currentValue = 278335.7898,
                totalValue = 249.9956,
                concreteTotalValue = 250.0000,
                tax = 0.00000449,
            ),

            TradeMovement.buy(
                name = name,
                date = "11/05/2021",
                operatedAmount = 0.00033589,
                currentValue = 297713.4899,
                totalValue = 99.9989,
                concreteTotalValue = 100.0000,
                tax = 0.00000167,
            ),

            TradeMovement.buy(
                name = name,
                date = "12/05/2021",
                operatedAmount = 0.00018482,
                currentValue = 270529.4099,
                totalValue = 49.9992,
                concreteTotalValue = 50.0000,
                tax = 0.00000092,
            ),

            TradeMovement.buy(
                name = name,
                date = "12/05/2021",
                operatedAmount = 0.00007843,
                currentValue = 254999.9400,
                totalValue = 19.9996,
                concreteTotalValue = 20.0000,
                tax = 0.00000039,
            ),

            TradeMovement.buy(
                name = name,
                date = "17/05/2021",
                operatedAmount = 0.00004168,
                currentValue = 239891.1000,
                totalValue = 9.9986,
                concreteTotalValue = 10.0000,
                tax = 0.00000020,
            ),

            TradeMovement.buy(
                name = name,
                date = "21/06/2021",
                operatedAmount = 0.00092040,
                currentValue = 162954.5600,
                totalValue = 149.9833,
                concreteTotalValue = 150.0000,
                tax = 0.00000460,
            ),

            TradeMovement.buy(
                name = name,
                date = "17/07/2021",
                operatedAmount = 0.00089559,
                currentValue = 167477.3299,
                totalValue = 149.9910,
                concreteTotalValue = 150.0000,
                tax = 0.00000447,
            ),

            TradeMovement.buy(
                name = name,
                date = "20/07/2021",
                operatedAmount = 0.00019868,
                currentValue = 157129.3299,
                totalValue = 31.2184,
                tax = 0.00000099,
            ),

            TradeMovement.sell(
                name = name,
                date = "16/09/2021",
                operatedAmount = 0.00389328,
                currentValue = 252537.5200,
                totalValue = 983.1992,
                tax = 2.4579,
            ),

            TradeMovement.buy(
                name = name,
                date = "20/09/2021",
                operatedAmount = 0.00039404,
                currentValue = 236565.9499,
                totalValue = 93.2164,
                tax = 0.00000197
            ),

            TradeMovement.buy(
                name = name,
                date = "24/09/2021",
                operatedAmount = 0.0022644,
                currentValue = 220999.7700,
                totalValue = 499.9987,
                concreteTotalValue = 500.0000,
                tax = 0.00001131
            ),

            TradeMovement.sell(
                name = name,
                date = "26/09/2021",
                operatedAmount = 0.00264320,
                currentValue = 237099.9900,
                totalValue = 626.7026,
                tax = 1.5667
            ),

            TradeMovement.buy(
                name = name,
                date = "05/10/2021",
                operatedAmount = 0.00040507,
                currentValue = 271799.9998,
                totalValue = 110.0982,
                tax = 0.00000202
            ),

            TradeMovement.buy(
                name = name,
                date = "05/10/2021",
                operatedAmount = 0.00003000,
                currentValue = 271799.9993,
                totalValue = 8.1539,
                tax = 0.00000015
            ),

            TradeMovement.sell(
                name = name,
                date = "05/10/2021",
                operatedAmount = 0.00043289,
                currentValue = 273800.0000,
                totalValue = 118.5252,
                tax = .2963
            ),

            TradeMovement.buy(
                name = name,
                date = "06/10/2021",
                operatedAmount = 0.00061584,
                currentValue = 297800.9600,
                totalValue = 183.3977,
                tax = .00000153
            ),

            TradeMovement.sell(
                name = name,
                date = "11/10/2021",
                operatedAmount = 0.00061430,
                currentValue = 318727.8400,
                totalValue = 195.7945,
                tax = .4894
            ),

            TradeMovement.buy(
                name = name,
                date = "26/10/2021",
                operatedAmount = 0.00057798,
                currentValue = 346028.5100,
                totalValue = 199.9975,
                concreteTotalValue = 200.0000,
                tax = .00000144
            ),

            TradeMovement.buy(
                name = name,
                date = "05/11/2021",
                operatedAmount = 0.00289854,
                currentValue = 345000.6700,
                totalValue = 999.9982,
                concreteTotalValue = 1000.0000,
                tax = .00000724
            ),

            TradeMovement.sell(
                name = name,
                date = "09/11/2021",
                operatedAmount = 0.00346781,
                currentValue = 372000.0000,
                totalValue = 1290.0290,
                tax = 3.2250
            ),

            TradeMovement.buy(
                name = name,
                date = "16/11/2021",
                operatedAmount = 0.00105000,
                currentValue = 336512.5600,
                totalValue = 353.3381,
                tax = 0.00000262
            ),

            TradeMovement.buy(
                name = name,
                date = "16/11/2021",
                operatedAmount = 0.00117874,
                currentValue = 336512.5600,
                totalValue = 396.6608,
                tax = 0.00000294
            ),

            TradeMovement.buy(
                name = name,
                date = "18/11/2021",
                operatedAmount = 0.00310559,
                currentValue = 320999.7397,
                totalValue = 996.8935,
                tax = 0.00001552
            ),

            TradeMovement.buy(
                name = name,
                date = "26/11/2021",
                operatedAmount = 0.00126151,
                currentValue = 308121.53,
                totalValue = 388.7014,
                tax = 0.00000315
            ),

            TradeMovement.buy(
                name = name,
                date = "26/11/2021",
                operatedAmount = 0.00036624,
                currentValue = 308121.5300,
                totalValue = 112.8464,
                tax = 0.00000091
            ),

            TradeMovement.buy(
                name = name,
                date = "04/12/2021",
                operatedAmount = 0.00185185,
                currentValue = 270000.0000,
                totalValue = 499.9995,
                concreteTotalValue = 500.0000,
                tax = 0.00000462
            ),

            TradeMovement.buy(
                name = name,
                date = "28/12/2021",
                operatedAmount = 0.00225116,
                currentValue = 277502.45,
                totalValue = 624.7024,
                tax = 0.00000562
            ),

            TradeMovement.buy(
                name = name,
                date = "28/12/2021",
                operatedAmount = 0.00097609,
                currentValue = 277502.45,
                totalValue = 270.8673,
                tax = 0.00000244
            ),

            TradeMovement.buy(
                name = name,
                date = "22/01/2021",
                operatedAmount = 0.00249435,
                currentValue = 200449.8798,
                totalValue = 499.9921,
                concreteTotalValue = 500.0000,
                tax = 0.00001247
            )
        )

        dao?.insert(arr)
    }

    private fun addEth() {
        val name = "ETH"
        val arr = arrayListOf(
            TradeMovement.buy(
                name = name,
                date = "24/04/2021",
                operatedAmount = 0.00400535,
                currentValue = 12478.5699,
                totalValue = 49.9810,
                concreteTotalValue = 50.0000,
                tax = 0.00002002,
            ),

            TradeMovement.buy(
                name = name,
                date = "25/04/2021",
                operatedAmount = 0.00392112,
                currentValue = 12750.0699,
                totalValue = 49.9945,
                concreteTotalValue = 50.0000,
                tax = 0.00001960,
            ),

            TradeMovement.sell(
                name = name,
                date = "04/08/2021",
                operatedAmount = 0.00788682,
                currentValue = 14021.1800,
                totalValue = 110.5826,
                tax = 0.5529,
            )
        )

        dao?.insert(arr)
    }

    private fun addLtc() {
        val name = "LTC"
        val arr = arrayListOf(
            TradeMovement.buy(
                name = name,
                date = "11/05/2021",
                operatedAmount = 0.05134023,
                currentValue = 1947.5500,
                totalValue = 99.9876,
                concreteTotalValue = 100.0000,
                tax = 0.00025670,
            ),

            TradeMovement.buy(
                name = name,
                date = "11/05/2021",
                operatedAmount = 0.01005025,
                currentValue = 1990.0000,
                totalValue = 19.9999,
                concreteTotalValue = 20.0000,
                tax = 0.00005025,
            )
        )

        dao?.insert(arr)
    }

    private fun addZrx() {
        val name = "ZRX"
        val arr = arrayListOf(
            TradeMovement.buy(
                name = name,
                date = "17/07/2021",
                operatedAmount = 10.00000000,
                currentValue = 3.4800,
                totalValue = 34.8000,
                tax = 0.05000000,
            ),

            TradeMovement.buy(
                name = name,
                date = "20/07/2021",
                operatedAmount = 10.17964072,
                currentValue = 3.3450,
                totalValue = 34.0508,
                tax = 0.05089820,
            ),

            TradeMovement.sell(
                name = name,
                date = "04/08/2021",
                operatedAmount = 10.00000000,
                currentValue = 4.2140,
                totalValue = 42.1400,
                tax = 0.2107,
            ),

            TradeMovement.buy(
                name = name,
                date = "12/08/2021",
                operatedAmount = 11.27145965,
                currentValue = 4.9000,
                totalValue = 55.2301,
                tax = 0.02817864,
            ),

            TradeMovement.buy(
                name = name,
                date = "17/08/2021",
                operatedAmount = 4.08387883,
                currentValue = 5.5100,
                totalValue = 22.5021,
                tax = 0.01020969,
            ),

            TradeMovement.buy(
                name = name,
                date = "17/08/2021",
                operatedAmount = 1.24697416,
                currentValue = 5.5100,
                totalValue = 6.8708,
                tax = 0.00311743,
            ),

            TradeMovement.buy(
                name = name,
                date = "18/08/2021",
                operatedAmount = 12.95480769,
                currentValue = 5.2000,
                totalValue = 67.3649,
                tax = 0.03238701,
            ),

            TradeMovement.sell(
                name = name,
                date = "16/09/2021",
                operatedAmount = 28.66279911,
                currentValue = 5.9400,
                totalValue = 170.2570,
                tax = 0.4526,
            ),

            TradeMovement.sell(
                name = name,
                date = "16/09/2021",
                operatedAmount = 10.89917093,
                currentValue = 5.9400,
                totalValue = 64.7410,
                tax = 0.1618,
            ),

            TradeMovement.buy(
                name = name,
                date = "19/09/2021",
                operatedAmount = 66.94618182,
                currentValue = 5.5000,
                totalValue = 368.2040,
                tax = 0.16736545
            ),

            TradeMovement.buy(
                name = name,
                date = "19/09/2021",
                operatedAmount = 85.00000000,
                currentValue = 5.5000,
                totalValue = 467.5000,
                tax = 0.21250000
            ),

            TradeMovement.buy(
                name = name,
                date = "19/09/2021",
                operatedAmount = 21.26327272,
                currentValue = 5.5000,
                totalValue = 116.9479,
                tax = 0.05315818
            ),

            TradeMovement.buy(
                name = name,
                date = "20/09/2021",
                operatedAmount = 83.00000000,
                currentValue = 5.0000,
                totalValue = 415.0000,
                tax = 0.20750000
            ),

            TradeMovement.buy(
                name = name,
                date = "20/09/2021",
                operatedAmount = 1.73400000,
                currentValue = 5.0000,
                totalValue = 8.6700,
                tax = 0.00433500
            ),

            TradeMovement.buy(
                name = name,
                date = "20/09/2021",
                operatedAmount = 23.61016949,
                currentValue = 5.0000,
                totalValue = 118.0508,
                tax = 0.05902542
            ),

            TradeMovement.buy(
                name = name,
                date = "20/09/2021",
                operatedAmount = 10.00000000,
                currentValue = 5.0000,
                totalValue = 50.0000,
                tax = 0.02500000
            ),

            TradeMovement.buy(
                name = name,
                date = "20/09/2021",
                operatedAmount = 3.00925209,
                currentValue = 5.0000,
                totalValue = 15.0462,
                tax = 0.00752313
            ),

            TradeMovement.buy(
                name = name,
                date = "22/09/2021",
                operatedAmount = 53.76301075,
                currentValue = 4.6500,
                totalValue = 249.9979,
                concreteTotalValue = 250.0000,
                tax = 0.13440752
            ),

            TradeMovement.buy(
                name = name,
                date = "27/09/2021",
                operatedAmount = 82.00000000,
                currentValue = 5.1890,
                totalValue = 425.4980,
                tax = 0.41000000
            ),

            TradeMovement.buy(
                name = name,
                date = "27/09/2021",
                operatedAmount = 21.76611279,
                currentValue = 5.1900,
                totalValue = 112.9661,
                tax = 0.10883056
            ),

            TradeMovement.buy(
                name = name,
                date = "27/09/2021",
                operatedAmount = 16.67408136,
                currentValue = 5.1980,
                totalValue = 86.6718,
                tax = 0.08337040
            ),

            TradeMovement.sell(
                name = name,
                date = "03/10/2021",
                operatedAmount = 467.29306527,
                currentValue = 5.2800,
                totalValue = 2467.3073,
                tax = 6.1682
            ),

            TradeMovement.buy(
                name = name,
                date = "12/11/2021",
                operatedAmount = 27.07333333,
                currentValue = 6.9000,
                totalValue = 186.8059,
                tax = 0.06768333
            ),

            TradeMovement.buy(
                name = name,
                date = "10/12/2021",
                operatedAmount = 44.44644444,
                currentValue = 4.5000,
                totalValue = 200.0089,
                tax = 0.11111611
            )
        )

        dao?.insert(arr)
    }

    private fun addSlp() {
        val name = "SLP"
        val arr = arrayListOf(
            TradeMovement.buy(
                name = name,
                date = "23/09/2021",
                operatedAmount = 1585.67019027,
                currentValue = .4730,
                totalValue = 750.0219,
                tax = 7.92835095
            ),

            TradeMovement.buy(
                name = name,
                date = "24/09/2021",
                operatedAmount = 464.89637307,
                currentValue = .3860,
                totalValue = 179.4500,
                tax = 2.32448186
            ),

            TradeMovement.buy(
                name = name,
                date = "24/09/2021",
                operatedAmount = 500.00000000,
                currentValue = .3870,
                totalValue = 193.5000,
                tax = 2.50000000
            ),

            TradeMovement.buy(
                name = name,
                date = "24/09/2021",
                operatedAmount = 500.00000000,
                currentValue = .3880,
                totalValue = 194.0000,
                tax = 2.50000000
            ),

            TradeMovement.buy(
                name = name,
                date = "24/09/2021",
                operatedAmount = 500.00000000,
                currentValue = .3890,
                totalValue = 194.5000,
                tax = 2.50000000
            ),

            TradeMovement.buy(
                name = name,
                date = "24/09/2021",
                operatedAmount = 500.00000000,
                currentValue = .3890,
                totalValue = 194.5000,
                tax = 2.50000000
            ),

            TradeMovement.buy(
                name = name,
                date = "24/09/2021",
                operatedAmount = 112.66240407,
                currentValue = .3910,
                totalValue = 44.0509,
                tax = 0.56331202
            ),

            TradeMovement.sell(
                name = name,
                date = "04/10/2021",
                operatedAmount = 1108.75963718,
                currentValue = .4410,
                totalValue = 488.9629,
                tax = 1.2224
            ),

            TradeMovement.sell(
                name = name,
                date = "04/10/2021",
                operatedAmount = 22.67573696,
                currentValue = .4410,
                totalValue = 9.9999,
                tax = .0249
            ),

            TradeMovement.sell(
                name = name,
                date = "04/10/2021",
                operatedAmount = 3010.97744843,
                currentValue = .4410,
                totalValue = 1327.8410,
                tax = 3.3196
            ),

            TradeMovement.buy(
                name = name,
                date = "06/10/2021",
                operatedAmount = 1890.00666666,
                currentValue = .4500,
                totalValue = 850.5029,
                tax = 4.72501666
            ),

            TradeMovement.buy(
                name = name,
                date = "06/10/2021",
                operatedAmount = 40.59706953,
                currentValue = .4490,
                totalValue = 18.2280,
                tax = 0.10149267
            ),

            TradeMovement.buy(
                name = name,
                date = "06/10/2021",
                operatedAmount = 2149.10988018,
                currentValue = .4490,
                totalValue = 964.9503,
                tax = 5.37277470
            ),

            TradeMovement.buy(
                name = name,
                date = "06/10/2021",
                operatedAmount = 550.98347344,
                currentValue = .4490,
                totalValue = 247.3915,
                tax = 1.37745868
            ),

            TradeMovement.buy(
                name = name,
                date = "09/10/2021",
                operatedAmount = 5301.29791666,
                currentValue = .4800,
                totalValue = 2544.6229,
                tax = 13.25304479
            ),

            TradeMovement.buy(
                name = name,
                date = "11/10/2021",
                operatedAmount = 43.58353510,
                currentValue = .4130,
                totalValue = 17.9999,
                tax = 0.10895883
            ),

            TradeMovement.buy(
                name = name,
                date = "11/10/2021",
                operatedAmount = 67.52988461,
                currentValue = .4130,
                totalValue = 27.8898,
                tax = 0.16882471
            ),

            TradeMovement.buy(
                name = name,
                date = "11/10/2021",
                operatedAmount = 361.78004275,
                currentValue = .4130,
                totalValue = 149.4151,
                tax = 0.90445010
            ),

            TradeMovement.buy(
                name = name,
                date = "24/10/2021",
                operatedAmount = 800.00533333,
                currentValue = .3750,
                totalValue = 300.0019,
                tax = 2.00001333
            ),

            TradeMovement.sell(
                name = name,
                date = "29/10/2021",
                operatedAmount = 800.00000000,
                currentValue = .4000,
                totalValue = 320.0000,
                tax = 0.8000
            ),

            TradeMovement.sell(
                name = name,
                date = "04/11/2021",
                operatedAmount = 10376.86402391,
                currentValue = .5700,
                totalValue = 5914.8124,
                tax = 14.7870
            ),

            TradeMovement.buy(
                name = name,
                date = "11/11/2021",
                operatedAmount = 1707.31707317,
                currentValue = .4100,
                totalValue = 699.9999,
                concreteTotalValue = 700.0000,
                tax = 0.06768333
            ),

            TradeMovement.buy(
                name = name,
                date = "22/11/2021",
                operatedAmount = 1344.64879356,
                currentValue = .3730,
                totalValue = 501.5539,
                tax = 3.36162198
            ),

            TradeMovement.buy(
                name = name,
                date = "30/11/2021",
                operatedAmount = 2941.19117647,
                currentValue = .3400,
                totalValue = 1000.0049,
                tax = 14.70595588
            ),

            TradeMovement.buy(
                name = name,
                date = "05/12/2021",
                operatedAmount = 2477.44615385,
                currentValue = .2590,
                totalValue = 641.6585,
                tax = 12.38723076
            ),

            TradeMovement.buy(
                name = name,
                date = "10/12/2021",
                operatedAmount = 2727.27272727,
                currentValue = .2200,
                totalValue = 599.9999,
                concreteTotalValue = 600.0000,
                tax = 6.81818181
            ),

            TradeMovement.buy(
                name = name,
                date = "16/12/2021",
                operatedAmount = 105.93433333,
                currentValue = .2000,
                totalValue = 21.1868,
                tax = 0.26483583
            ),

            TradeMovement.buy(
                name = name,
                date = "16/12/2021",
                operatedAmount = 2394.06566667,
                currentValue = .2000,
                totalValue = 478.8131,
                tax = 5.98516416
            ),

            TradeMovement.buy(
                name = name,
                date = "19/12/2021",
                operatedAmount = 4081.46842105,
                currentValue = .1900,
                totalValue = 775.4789,
                tax = 10.20367105
            ),

            TradeMovement.sell(
                name = name,
                date = "21/12/2021",
                operatedAmount = 4081.00000000,
                currentValue = .2200,
                totalValue = 897.8200,
                tax = 2.2445
            ),
        )

        dao?.insert(arr)
    }

    private fun addKnc() {
        val name = "KNC"
        val arr = arrayListOf(
            TradeMovement.buy(
                name = name,
                date = "19/09/2021",
                operatedAmount = 10.46783153,
                currentValue = 9.6999,
                totalValue = 101.5369,
                tax = 0.02616957,
            ),

            TradeMovement.buy(
                name = name,
                date = "20/09/2021",
                operatedAmount = 16.59402413,
                currentValue = 9.6999,
                totalValue = 160.9603,
                tax = 0.04148506
            ),

            TradeMovement.buy(
                name = name,
                date = "03/10/2021",
                operatedAmount = 116.89070718,
                currentValue = 8.5550,
                totalValue = 999.9999,
                concreteTotalValue = 1000.0000,
                tax = 0.29222676
            ),

            TradeMovement.buy(
                name = name,
                date = "04/10/2021",
                operatedAmount = 81.61748412,
                currentValue = 8.5000,
                totalValue = 693.7486,
                tax = 0.20404371
            ),

            TradeMovement.buy(
                name = name,
                date = "04/10/2021",
                operatedAmount = 2.81318233,
                currentValue = 8.5000,
                totalValue = 23.9120,
                tax = 0.00703295
            ),

            TradeMovement.buy(
                name = name,
                date = "04/10/2021",
                operatedAmount = 57.01704619,
                currentValue = 8.5000,
                totalValue = 484.6193,
                tax = 0.14253511
            ),

            TradeMovement.sell(
                name = name,
                date = "06/10/2021",
                operatedAmount = 17.13091797,
                currentValue = 9.6999,
                totalValue = 166.1681,
                tax = 0.4154
            ),

            TradeMovement.sell(
                name = name,
                date = "06/10/2021",
                operatedAmount = 1.53072165,
                currentValue = 9.6999,
                totalValue = 14.8478,
                tax = 0.0371
            ),

            TradeMovement.sell(
                name = name,
                date = "06/10/2021",
                operatedAmount = 0.29402150,
                currentValue = 9.6999,
                totalValue = 2.8519,
                tax = 0.0071
            ),

            TradeMovement.sell(
                name = name,
                date = "08/10/2021",
                operatedAmount = 263.97424794,
                currentValue = 9.6000,
                totalValue = 2534.1527,
                tax = 6.3353
            ),

            TradeMovement.sell(
                name = name,
                date = "08/10/2021",
                operatedAmount = 1.75387334,
                currentValue = 9.6000,
                totalValue = 16.8371,
                tax = 0.0420
            ),

            TradeMovement.buy(
                name = name,
                date = "10/11/2021",
                operatedAmount = 16.97090096,
                currentValue = 10.7000,
                totalValue = 181.5886,
                tax = 0.04242725
            ),

            TradeMovement.buy(
                name = name,
                date = "10/11/2021",
                operatedAmount = 4.21271487,
                currentValue = 10.7000,
                totalValue = 45.0760,
                tax = 0.01053178
            ),

            TradeMovement.buy(
                name = name,
                date = "10/11/2021",
                operatedAmount = 16.19956174,
                currentValue = 10.7000,
                totalValue = 173.3353,
                tax = 0.04049890
            ),

            TradeMovement.sell(
                name = name,
                date = "10/11/2021",
                operatedAmount = 37.28971962,
                currentValue = 11.5,
                totalValue = 428.83177563,
                tax = 1.072079439075
            )
        )

        dao?.insert(arr)
    }

    private fun addAda() {
        val name = "ADA"
        val arr = arrayListOf(
            TradeMovement.buy(
                name = name,
                date = "30/10/2021",
                operatedAmount = 28.04920913,
                currentValue = 11.3800,
                totalValue = 319.1999,
                concreteTotalValue = 320.0000,
                tax = 0.07012302
            ),

            TradeMovement.sell(
                name = name,
                date = "09/11/2021",
                operatedAmount = 27.97828611,
                currentValue = 12.5000,
                totalValue = 349.7285,
                tax = 0.8743
            )
        )

        dao?.insert(arr)
    }

    private fun addDot() {
        val name = "DOT"
        val arr = arrayListOf(
            TradeMovement.buy(
                name = name,
                date = "05/11/2021",
                operatedAmount = 3.06560392,
                currentValue = 293.5800,
                totalValue = 899.9999,
                concreteTotalValue = 900.0000,
                tax = .00766400
            ),

            TradeMovement.buy(
                name = name,
                date = "10/11/2021",
                operatedAmount = 4.81742856,
                currentValue = 280.0000,
                totalValue = 1348.8799,
                tax = .01204357
            ),

            TradeMovement.buy(
                name = name,
                date = "16/11/2021",
                operatedAmount = 1.08695652,
                currentValue = 230.0000,
                totalValue = 249.9999,
                concreteTotalValue = 300.0000,
                tax = 0.00271739
            )
        )

        dao?.insert(arr)
    }

    private fun addMatic() {
        val name = "MATIC"
        val arr = arrayListOf(
            TradeMovement.buy(
                name = name,
                date = "04/12/2021",
                operatedAmount = 14.46523040,
                currentValue = 9.5000,
                totalValue = 137.4196,
                tax = 0.03616307
            ),

            TradeMovement.buy(
                name = name,
                date = "04/12/2021",
                operatedAmount = 38.16634855,
                currentValue = 9.5000,
                totalValue = 362.5803,
                tax = 0.09541587
            ),

            TradeMovement.sell(
                name = name,
                date = "04/12/2021",
                operatedAmount = 52.50000000,
                currentValue = 12.3000,
                totalValue = 645.7500,
                tax = 1.6143
            ),

            TradeMovement.buy(
                name = name,
                date = "13/12/2021",
                operatedAmount = 15.35704934,
                currentValue = 11.5000,
                totalValue = 176.6060,
                tax = 0.03839262
            ),

            TradeMovement.buy(
                name = name,
                date = "13/12/2021",
                operatedAmount = 0.45258069,
                currentValue = 11.5000,
                totalValue = 5.2046,
                tax = 0.00113145
            ),

            TradeMovement.buy(
                name = name,
                date = "13/12/2021",
                operatedAmount = 1.58167432,
                currentValue = 11.5000,
                totalValue = 18.1892,
                tax = 0.00395418
            ),

            TradeMovement.buy(
                name = name,
                date = "13/12/2021",
                operatedAmount = 42.35240032,
                currentValue = 11.3000,
                totalValue = 478.5821,
                tax = 0.10588100
            ),

            TradeMovement.buy(
                name = name,
                date = "13/12/2021",
                operatedAmount = 2.11388287,
                currentValue = 11.3000,
                totalValue = 23.8868,
                tax = 0.00528470
            ),

            TradeMovement.sell(
                name = name,
                date = "18/12/2021",
                operatedAmount = 0.11721355,
                currentValue = 12.7000,
                totalValue = 1.4886,
                tax = 0.0037
            ),

            TradeMovement.sell(
                name = name,
                date = "18/12/2021",
                operatedAmount = 1.57472441,
                currentValue = 12.7000,
                totalValue = 19.999,
                tax = 0.0499
            ),

            TradeMovement.sell(
                name = name,
                date = "19/12/2021",
                operatedAmount = 0.53958251,
                currentValue = 12.7000,
                totalValue = 6.8526,
                tax = 0.0171
            ),

            TradeMovement.sell(
                name = name,
                date = "19/12/2021",
                operatedAmount = 1.00000000,
                currentValue = 12.700,
                totalValue = 12.7000,
                tax = 0.0317
            ),

            TradeMovement.sell(
                name = name,
                date = "19/12/2021",
                operatedAmount = 1.96850394,
                currentValue = 12.7000,
                totalValue = 25.0000,
                tax = 0.0625
            ),

            TradeMovement.sell(
                name = name,
                date = "19/12/2021",
                operatedAmount = 56.50213945,
                currentValue = 12.6220,
                totalValue = 713.1700,
                tax = 3.5658
            ),

            TradeMovement.buy(
                name = name,
                date = "22/01/2022",
                operatedAmount = 53.31210150,
                currentValue = 9.3790,
                totalValue = 500.0141,
                tax = 0.26656050
            ),
        )

        dao?.insert(arr)
    }

    private fun addDeposit() {
        val arr = arrayListOf(
            TradeMovement.deposit(
                date = "24/04/2021",
                totalValue = 50.00
            ),

            TradeMovement.deposit(
                date = "24/04/2021",
                totalValue = 100.00
            ),

            TradeMovement.deposit(
                date = "25/04/2021",
                totalValue = 300.00
            ),

            TradeMovement.deposit(
                date = "11/05/2021",
                totalValue = 300.00
            ),

            TradeMovement.deposit(
                date = "21/06/2021",
                totalValue = 150.00
            ),

            TradeMovement.deposit(
                date = "17/07/2021",
                totalValue = 250.00
            ),

            TradeMovement.deposit(
                date = "20/09/2021",
                totalValue = 700.00
            ),

            TradeMovement.deposit(
                date = "21/09/2021",
                totalValue = 1000.00
            ),

            TradeMovement.deposit(
                date = "24/09/2021",
                totalValue = 500.00
            ),

            TradeMovement.deposit(
                date = "24/09/2021",
                totalValue = 1000.00
            ),

            TradeMovement.deposit(
                date = "24/10/2021",
                totalValue = 500.00
            ),

            TradeMovement.deposit(
                date = "16/11/2021",
                totalValue = 1000.00
            ),

            TradeMovement.deposit(
                date = "18/11/2021",
                totalValue = 2000.00
            ),
            TradeMovement.deposit(
                date = "30/11/2021",
                totalValue = 1000.00
            ),

            TradeMovement.deposit(
                date = "04/12/2021",
                totalValue = 1000.00
            ),

            TradeMovement.deposit(
                date = "07/12/2021",
                totalValue = 1500.00
            ),

            TradeMovement.deposit(
                date = "15/12/2021",
                totalValue = 500.00
            ),

            TradeMovement.deposit(
                date = "22/01/2022",
                totalValue = 1000.00
            )
        )
        dao?.insert(arr)
    }

    private fun addWithdraw() {
        val arr = arrayListOf(
            TradeMovement.withdraw(
                date = "05/11/2021",
                totalValue = 3000.0
            )
        )
        dao?.insert(arr)
    }
}
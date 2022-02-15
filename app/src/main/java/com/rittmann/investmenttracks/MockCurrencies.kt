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
                currentValue = 278435.28,
                totalValue = 49.99,
                tax = 0.00000089,
            ),

            TradeMovement.buy(
                name = name,
                date = "24/04/2021",
                operatedAmount = 0.00017963,
                currentValue = 278357.00,
                totalValue = 49.99,
                tax = 0.00000089,
            ),

            TradeMovement.buy(
                name = name,
                date = "25/04/2021",
                operatedAmount = 0.00089818,
                currentValue = 278335.78,
                totalValue = 249.99,
                tax = 0.00000449,
            ),

            TradeMovement.buy(
                name = name,
                date = "11/05/2021",
                operatedAmount = 0.00033589,
                currentValue = 297713.48,
                totalValue = 99.99,
                tax = 0.00000167,
            ),

            TradeMovement.buy(
                name = name,
                date = "12/05/2021",
                operatedAmount = 0.00018482,
                currentValue = 270529.40,
                totalValue = 49.99,
                tax = 0.00000092,
            ),

            TradeMovement.buy(
                name = name,
                date = "12/05/2021",
                operatedAmount = 0.00007843,
                currentValue = 254999.94,
                totalValue = 19.99,
                tax = 0.00000039,
            ),

            TradeMovement.buy(
                name = name,
                date = "17/05/2021",
                operatedAmount = 0.00004168,
                currentValue = 239891.10,
                totalValue = 9.99,
                tax = 0.00000020,
            ),

            TradeMovement.buy(
                name = name,
                date = "21/06/2021",
                operatedAmount = 0.00092040,
                currentValue = 162954.56,
                totalValue = 149.98,
                tax = 0.00000460,
            ),

            TradeMovement.buy(
                name = name,
                date = "17/07/2021",
                operatedAmount = 0.00089559,
                currentValue = 167477.32,
                totalValue = 149.99,
                tax = 0.00000447,
            ),

            TradeMovement.buy(
                name = name,
                date = "20/07/2021",
                operatedAmount = 0.00019868,
                currentValue = 157129.32,
                totalValue = 31.21,
                tax = 0.00000099,
            ),

            TradeMovement.sell(
                name = name,
                date = "16/09/2021",
                operatedAmount = 0.00389328,
                currentValue = 252537.52,
                totalValue = 983.19,
                tax = 2.45,
            ),

            TradeMovement.buy(
                name = name,
                date = "20/09/2021",
                operatedAmount = 0.00039404,
                currentValue = 236565.94,
                totalValue = 93.21,
                tax = 0.00000197
            ),

            TradeMovement.buy(
                name = name,
                date = "24/09/2021",
                operatedAmount = 0.0022644,
                currentValue = 220999.77,
                totalValue = 499.99,
                tax = 0.00001131
            ),

            TradeMovement.sell(
                name = name,
                date = "26/09/2021",
                operatedAmount = 0.00264320,
                currentValue = 237099.99,
                totalValue = 626.70,
                tax = 1.56
            ),

            TradeMovement.buy(
                name = name,
                date = "05/10/2021",
                operatedAmount = 0.00040507,
                currentValue = 271799.99,
                totalValue = 110.09,
                tax = 0.00000202
            ),

            TradeMovement.buy(
                name = name,
                date = "05/10/2021",
                operatedAmount = 0.00003000,
                currentValue = 271799.99,
                totalValue = 8.15,
                tax = 0.00000015
            ),

            TradeMovement.sell(
                name = name,
                date = "05/10/2021",
                operatedAmount = 0.00043289,
                currentValue = 273800.00,
                totalValue = 118.52,
                tax = .29
            ),

            TradeMovement.buy(
                name = name,
                date = "06/10/2021",
                operatedAmount = 0.00061584,
                currentValue = 297800.96,
                totalValue = 183.39,
                tax = .00000153
            ),

            TradeMovement.sell(
                name = name,
                date = "11/10/2021",
                operatedAmount = 0.00061430,
                currentValue = 318727.84,
                totalValue = 195.79,
                tax = .48
            ),

            TradeMovement.buy(
                name = name,
                date = "26/10/2021",
                operatedAmount = 0.00057798,
                currentValue = 346028.51,
                totalValue = 199.99,
                tax = .00000144
            ),

            TradeMovement.buy(
                name = name,
                date = "05/11/2021",
                operatedAmount = 0.00289854,
                currentValue = 345000.67,
                totalValue = 999.99,
                tax = .00000724
            ),

            TradeMovement.sell(
                name = name,
                date = "09/11/2021",
                operatedAmount = 0.00346781,
                currentValue = 372000.00,
                totalValue = 1290.02,
                tax = 3.22
            ),

            TradeMovement.buy(
                name = name,
                date = "16/11/2021",
                operatedAmount = 0.00105000,
                currentValue = 336512.56,
                totalValue = 353.33,
                tax = 0.00000262
            ),

            TradeMovement.buy(
                name = name,
                date = "16/11/2021",
                operatedAmount = 0.00117874,
                currentValue = 335512.56,
                totalValue = 249.99,
                tax = 0.00000294
            ),

            TradeMovement.buy(
                name = name,
                date = "18/11/2021",
                operatedAmount = 0.00310559,
                currentValue = 320999.73,
                totalValue = 996.89,
                tax = 0.00001552
            ),

            TradeMovement.buy(
                name = name,
                date = "04/12/2021",
                operatedAmount = 0.00185185,
                currentValue = 270000.00,
                totalValue = 499.99,
                tax = 0.00000462
            ),

            TradeMovement.buy(
                name = name,
                date = "26/11/2021",
                operatedAmount = 0.00036624,
                currentValue = 308121.53,
                totalValue = 112.84,
                tax = 0.00000091
            ),

            TradeMovement.buy(
                name = name,
                date = "26/11/2021",
                operatedAmount = 0.00126151,
                currentValue = 308121.53,
                totalValue = 388.70,
                tax = 0.00000315
            ),

            TradeMovement.buy(
                name = name,
                date = "28/12/2021",
                operatedAmount = 0.00225116,
                currentValue = 277502.45,
                totalValue = 624.70,
                tax = 0.00000562
            ),

            TradeMovement.buy(
                name = name,
                date = "28/12/2021",
                operatedAmount = 0.00097609,
                currentValue = 277502.45,
                totalValue = 270.86,
                tax = 0.00000244
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
                currentValue = 12478.56,
                totalValue = 49.98,
                tax = 0.00002002,
            ),

            TradeMovement.buy(
                name = name,
                date = "05/04/2021",
                operatedAmount = 0.00392112,
                currentValue = 12750.06,
                totalValue = 49.99,
                tax = 0.00001960,
            ),

            TradeMovement.sell(
                name = name,
                date = "04/08/2021",
                operatedAmount = 0.00788682,
                currentValue = 14021.18,
                totalValue = 110.58,
                tax = 0.55,
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
                currentValue = 1947.55,
                totalValue = 99.98,
                tax = 0.00025670,
            ),

            TradeMovement.buy(
                name = name,
                date = "11/05/2021",
                operatedAmount = 0.01005025,
                currentValue = 1990.00,
                totalValue = 19.99,
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
                currentValue = 3.480,
                totalValue = 34.800,
                tax = 0.05000000,
            ),

            TradeMovement.buy(
                name = name,
                date = "20/07/2021",
                operatedAmount = 10.17964072,
                currentValue = 3.345,
                totalValue = 34.050,
                tax = 0.05089820,
            ),

            TradeMovement.sell(
                name = name,
                date = "04/08/2021",
                operatedAmount = 10.00000000,
                currentValue = 4.214,
                totalValue = 42.140,
                tax = 0.210,
            ),

            TradeMovement.buy(
                name = name,
                date = "12/08/2021",
                operatedAmount = 11.27145965,
                currentValue = 4.900,
                totalValue = 55.230,
                tax = 0.02817864,
            ),

            TradeMovement.buy(
                name = name,
                date = "17/08/2021",
                operatedAmount = 4.08387883,
                currentValue = 5.510,
                totalValue = 22.502,
                tax = 0.01020969,
            ),

            TradeMovement.buy(
                name = name,
                date = "17/08/2021",
                operatedAmount = 1.24697416,
                currentValue = 5.510,
                totalValue = 6.870,
                tax = 0.00311743,
            ),

            TradeMovement.buy(
                name = name,
                date = "18/08/2021",
                operatedAmount = 12.95480769,
                currentValue = 5.200,
                totalValue = 67.364,
                tax = 0.03238701,
            ),

            TradeMovement.sell(
                name = name,
                date = "16/09/2021",
                operatedAmount = 28.66279911,
                currentValue = 5.940,
                totalValue = 170.257,
                tax = 0.452,
            ),

            TradeMovement.sell(
                name = name,
                date = "16/09/2021",
                operatedAmount = 10.89917093,
                currentValue = 5.940,
                totalValue = 64.741,
                tax = 0.161,
            ),

            TradeMovement.buy(
                name = name,
                date = "19/09/2021",
                operatedAmount = 66.94618182,
                currentValue = 5.500,
                totalValue = 368.204,
                tax = 0.16736545
            ),

            TradeMovement.buy(
                name = name,
                date = "19/09/2021",
                operatedAmount = 85.00000000,
                currentValue = 5.500,
                totalValue = 467.500,
                tax = 0.21250000
            ),

            TradeMovement.buy(
                name = name,
                date = "19/09/2021",
                operatedAmount = 21.26327272,
                currentValue = 5.500,
                totalValue = 116.947,
                tax = 0.05315818
            ),

            TradeMovement.buy(
                name = name,
                date = "20/09/2021",
                operatedAmount = 83.00000000,
                currentValue = 5.000,
                totalValue = 415.000,
                tax = 0.20750000
            ),

            TradeMovement.buy(
                name = name,
                date = "20/09/2021",
                operatedAmount = 1.73400000,
                currentValue = 5.000,
                totalValue = 8.670,
                tax = 0.00433500
            ),

            TradeMovement.buy(
                name = name,
                date = "20/09/2021",
                operatedAmount = 23.61016949,
                currentValue = 5.000,
                totalValue = 118.050,
                tax = 0.05902542
            ),

            TradeMovement.buy(
                name = name,
                date = "20/09/2021",
                operatedAmount = 10.00000000,
                currentValue = 5.000,
                totalValue = 50.000,
                tax = 0.02500000
            ),

            TradeMovement.buy(
                name = name,
                date = "20/09/2021",
                operatedAmount = 3.00925209,
                currentValue = 5.000,
                totalValue = 15.046,
                tax = 0.00752313
            ),

            TradeMovement.buy(
                name = name,
                date = "22/09/2021",
                operatedAmount = 53.76301075,
                currentValue = 4.650,
                totalValue = 249.997,
                tax = 0.13440752
            ),

            TradeMovement.buy(
                name = name,
                date = "27/09/2021",
                operatedAmount = 82.00000000,
                currentValue = 5.189,
                totalValue = 425.498,
                tax = 0.41000000
            ),

            TradeMovement.buy(
                name = name,
                date = "27/09/2021",
                operatedAmount = 21.76611279,
                currentValue = 5.190,
                totalValue = 112.966,
                tax = 0.10883056
            ),

            TradeMovement.buy(
                name = name,
                date = "27/09/2021",
                operatedAmount = 16.67408136,
                currentValue = 5.198,
                totalValue = 86.671,
                tax = 0.08337040
            ),

            TradeMovement.sell(
                name = name,
                date = "03/10/2021",
                operatedAmount = 467.29306527,
                currentValue = 5.280,
                totalValue = 2467.307,
                tax = 6.168
            ),

            TradeMovement.buy(
                name = name,
                date = "12/11/2021",
                operatedAmount = 27.07333333,
                currentValue = 6.900,
                totalValue = 186.805,
                tax = 0.06768333
            ),

            TradeMovement.buy(
                name = name,
                date = "10/12/2021",
                operatedAmount = 44.44644444,
                currentValue = 4.500,
                totalValue = 200.008,
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
                currentValue = .473,
                totalValue = 750.021,
                tax = 7.92835095
            ),

            TradeMovement.buy(
                name = name,
                date = "24/09/2021",
                operatedAmount = 464.89637307,
                currentValue = .386,
                totalValue = 179.450,
                tax = 2.32448186
            ),

            TradeMovement.buy(
                name = name,
                date = "24/09/2021",
                operatedAmount = 500.00000000,
                currentValue = .387,
                totalValue = 193.500,
                tax = 2.50000000
            ),

            TradeMovement.buy(
                name = name,
                date = "24/09/2021",
                operatedAmount = 500.00000000,
                currentValue = .388,
                totalValue = 194.000,
                tax = 2.50000000
            ),

            TradeMovement.buy(
                name = name,
                date = "24/09/2021",
                operatedAmount = 500.00000000,
                currentValue = .389,
                totalValue = 194.500,
                tax = 2.50000000
            ),

            TradeMovement.buy(
                name = name,
                date = "24/09/2021",
                operatedAmount = 500.00000000,
                currentValue = .389,
                totalValue = 194.500,
                tax = 2.50000000
            ),

            TradeMovement.buy(
                name = name,
                date = "24/09/2021",
                operatedAmount = 112.66240407,
                currentValue = .391,
                totalValue = 44.050,
                tax = 0.56331202
            ),

            TradeMovement.sell(
                name = name,
                date = "04/10/2021",
                operatedAmount = 1108.75963718,
                currentValue = .441,
                totalValue = 488.962,
                tax = 1.222
            ),

            TradeMovement.sell(
                name = name,
                date = "04/10/2021",
                operatedAmount = 22.67573696,
                currentValue = .441,
                totalValue = 9.999,
                tax = .024
            ),

            TradeMovement.sell(
                name = name,
                date = "04/10/2021",
                operatedAmount = 3010.97744843,
                currentValue = .441,
                totalValue = 1327.841,
                tax = 3.319
            ),

            TradeMovement.buy(
                name = name,
                date = "06/10/2021",
                operatedAmount = 1890.00666666,
                currentValue = .450,
                totalValue = 850.502,
                tax = 4.72501666
            ),

            TradeMovement.buy(
                name = name,
                date = "06/10/2021",
                operatedAmount = 40.59706953,
                currentValue = .449,
                totalValue = 18.228,
                tax = 0.10149267
            ),

            TradeMovement.buy(
                name = name,
                date = "06/10/2021",
                operatedAmount = 2149.10988018,
                currentValue = .449,
                totalValue = 964.950,
                tax = 5.37277470
            ),

            TradeMovement.buy(
                name = name,
                date = "06/10/2021",
                operatedAmount = 550.98347344,
                currentValue = .449,
                totalValue = 247.391,
                tax = 1.37745868
            ),

            TradeMovement.buy(
                name = name,
                date = "09/10/2021",
                operatedAmount = 5301.29791666,
                currentValue = .480,
                totalValue = 2544.622,
                tax = 13.25304479
            ),

            TradeMovement.buy(
                name = name,
                date = "11/10/2021",
                operatedAmount = 43.58353510,
                currentValue = .413,
                totalValue = 17.999,
                tax = 0.10895883
            ),

            TradeMovement.buy(
                name = name,
                date = "11/10/2021",
                operatedAmount = 67.52988461,
                currentValue = .413,
                totalValue = 27.889,
                tax = 0.16882471
            ),

            TradeMovement.buy(
                name = name,
                date = "11/10/2021",
                operatedAmount = 361.78004275,
                currentValue = .413,
                totalValue = 149.415,
                tax = 0.90445010
            ),

            TradeMovement.buy(
                name = name,
                date = "24/10/2021",
                operatedAmount = 800.00533333,
                currentValue = .375,
                totalValue = 300.001,
                tax = 2.00001333
            ),

            TradeMovement.sell(
                name = name,
                date = "29/10/2021",
                operatedAmount = 800.00000000,
                currentValue = .400,
                totalValue = 320.000,
                tax = 0.800
            ),

            TradeMovement.sell(
                name = name,
                date = "04/11/2021",
                operatedAmount = 10376.86402391,
                currentValue = .570,
                totalValue = 5914.812,
                tax = 14.787
            ),

            TradeMovement.buy(
                name = name,
                date = "11/11/2021",
                operatedAmount = 1707.31707317,
                currentValue = .410,
                totalValue = 699.99,
                tax = 0.06768333
            ),

            TradeMovement.buy(
                name = name,
                date = "10/12/2021",
                operatedAmount = 2727.27272727,
                currentValue = .220,
                totalValue = 599.999,
                tax = 6.81818181
            ),

            TradeMovement.buy(
                name = name,
                date = "05/12/2021",
                operatedAmount = 2477.44615385,
                currentValue = .259,
                totalValue = 641.658,
                tax = 12.38723076
            ),

            TradeMovement.buy(
                name = name,
                date = "30/11/2021",
                operatedAmount = 2941.19117647,
                currentValue = .340,
                totalValue = 1000.004,
                tax = 14.70595588
            ),

            TradeMovement.buy(
                name = name,
                date = "22/11/2021",
                operatedAmount = 1344.64879356,
                currentValue = .373,
                totalValue = 501.553,
                tax = 3.36162198
            ),

            TradeMovement.sell(
                name = name,
                date = "21/12/2021",
                operatedAmount = 4081.00000000,
                currentValue = .220,
                totalValue = 897.820,
                tax = 2.244
            ),

            TradeMovement.buy(
                name = name,
                date = "19/12/2021",
                operatedAmount = 4081.46842105,
                currentValue = .190,
                totalValue = 775.478,
                tax = 10.20367105
            ),

            TradeMovement.buy(
                name = name,
                date = "16/12/2021",
                operatedAmount = 2394.06566667,
                currentValue = .200,
                totalValue = 478.813,
                tax = 5.98516416
            ),

            TradeMovement.buy(
                name = name,
                date = "16/12/2021",
                operatedAmount = 105.93433333,
                currentValue = .200,
                totalValue = 21.186,
                tax = 0.26483583
            )
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
                currentValue = 9.699,
                totalValue = 101.536,
                tax = 0.02616957,
            ),

            TradeMovement.buy(
                name = name,
                date = "20/09/2021",
                operatedAmount = 16.59402413,
                currentValue = 9.699,
                totalValue = 160.960,
                tax = 0.04148506
            ),

            TradeMovement.buy(
                name = name,
                date = "03/10/2021",
                operatedAmount = 116.89070718,
                currentValue = 8.555,
                totalValue = 999.999,
                tax = 0.29222676
            ),

            TradeMovement.buy(
                name = name,
                date = "04/10/2021",
                operatedAmount = 81.61748412,
                currentValue = 8.500,
                totalValue = 693.748,
                tax = 0.20404371
            ),

            TradeMovement.buy(
                name = name,
                date = "04/10/2021",
                operatedAmount = 2.81318233,
                currentValue = 8.500,
                totalValue = 23.912,
                tax = 0.00703295
            ),

            TradeMovement.buy(
                name = name,
                date = "04/10/2021",
                operatedAmount = 57.01704619,
                currentValue = 8.500,
                totalValue = 484.619,
                tax = 0.14253511
            ),

            TradeMovement.sell(
                name = name,
                date = "06/10/2021",
                operatedAmount = 17.13091797,
                currentValue = 9.699,
                totalValue = 166.168,
                tax = 0.415
            ),

            TradeMovement.sell(
                name = name,
                date = "06/10/2021",
                operatedAmount = 1.53072165,
                currentValue = 9.699,
                totalValue = 14.847,
                tax = 0.037
            ),

            TradeMovement.sell(
                name = name,
                date = "06/10/2021",
                operatedAmount = 0.29402150,
                currentValue = 9.699,
                totalValue = 2.851,
                tax = 0.007
            ),

            TradeMovement.sell(
                name = name,
                date = "08/10/2021",
                operatedAmount = 263.97424794,
                currentValue = 9.600,
                totalValue = 2534.152,
                tax = 6.335
            ),

            TradeMovement.buy(
                name = name,
                date = "10/11/2021",
                operatedAmount = 16.97090096,
                currentValue = 10.700,
                totalValue = 181.588,
                tax = 0.04242725
            ),

            TradeMovement.buy(
                name = name,
                date = "10/11/2021",
                operatedAmount = 4.21271487,
                currentValue = 10.700,
                totalValue = 45.076,
                tax = 0.01053178
            ),

            TradeMovement.buy(
                name = name,
                date = "10/11/2021",
                operatedAmount = 16.19956174,
                currentValue = 10.700,
                totalValue = 173.335,
                tax = 0.04049890
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
                currentValue = 11.380,
                totalValue = 319.199,
                tax = 0.07012302
            ),

            TradeMovement.sell(
                name = name,
                date = "09/11/2021",
                operatedAmount = 27.97828611,
                currentValue = 12.500,
                totalValue = 349.728,
                tax = 0.874
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
                currentValue = 293.54,
                totalValue = 899.99,
                tax = .00766400
            ),

            TradeMovement.buy(
                name = name,
                date = "10/11/2021",
                operatedAmount = 4.81742856,
                currentValue = 280.00,
                totalValue = 1348.87,
                tax = .01204357
            ),

            TradeMovement.buy(
                name = name,
                date = "16/11/2021",
                operatedAmount = 1.08695652,
                currentValue = 230.00,
                totalValue = 249.99,
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
                date = "13/12/2021",
                operatedAmount = 1.58167432,
                currentValue = 11.500,
                totalValue = 18.189,
                tax = 0.00395418
            ),

            TradeMovement.buy(
                name = name,
                date = "13/12/2021",
                operatedAmount = 0.45258069,
                currentValue = 11.500,
                totalValue = 5.204,
                tax = 0.00113145
            ),

            TradeMovement.buy(
                name = name,
                date = "13/12/2021",
                operatedAmount = 15.35704934,
                currentValue = 11.500,
                totalValue = 176.606,
                tax = 0.03839262
            ),

            TradeMovement.sell(
                name = name,
                date = "04/12/2021",
                operatedAmount = 52.50000000,
                currentValue = 12.300,
                totalValue = 645.750,
                tax = 1.614
            ),

            TradeMovement.buy(
                name = name,
                date = "04/12/2021",
                operatedAmount = 38.16634855,
                currentValue = 9.500,
                totalValue = 362.580,
                tax = 0.09541587
            ),

            TradeMovement.buy(
                name = name,
                date = "04/12/2021",
                operatedAmount = 14.46523040,
                currentValue = 9.500,
                totalValue = 137.419,
                tax = 0.03616307
            ),

            TradeMovement.sell(
                name = name,
                date = "19/12/2021",
                operatedAmount = 56.50213945,
                currentValue = 12.622,
                totalValue = 713.170,
                tax = 3.565
            ),

            TradeMovement.sell(
                name = name,
                date = "19/12/2021",
                operatedAmount = 1.96850394,
                currentValue = 12.700,
                totalValue = 25.000,
                tax = 0.062
            ),

            TradeMovement.sell(
                name = name,
                date = "19/12/2021",
                operatedAmount = 1.00000000,
                currentValue = 12.700,
                totalValue = 12.700,
                tax = 0.031
            ),

            TradeMovement.sell(
                name = name,
                date = "19/12/2021",
                operatedAmount = 0.53958251,
                currentValue = 12.700,
                totalValue = 6.852,
                tax = 0.017
            ),

            TradeMovement.sell(
                name = name,
                date = "18/12/2021",
                operatedAmount = 1.57472441,
                currentValue = 12.700,
                totalValue = 19.999,
                tax = 0.049
            ),

            TradeMovement.sell(
                name = name,
                date = "18/12/2021",
                operatedAmount = 0.11721355,
                currentValue = 12.700,
                totalValue = 1.488,
                tax = 0.003
            ),

            TradeMovement.buy(
                name = name,
                date = "13/12/2021",
                operatedAmount = 2.11388287,
                currentValue = 11.300,
                totalValue = 23.886,
                tax = 0.00528470
            ),

            TradeMovement.buy(
                name = name,
                date = "13/12/2021",
                operatedAmount = 42.35240032,
                currentValue = 11.300,
                totalValue = 478.582,
                tax = 0.10588100
            )
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

    private fun addWithdraw(){
        val arr = arrayListOf(
            TradeMovement.withdraw(
                date = "05/11/2021",
                totalValue = 3000.0
            )
        )
        dao?.insert(arr)
    }
}
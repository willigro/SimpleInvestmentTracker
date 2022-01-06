package com.rittmann.common.utils

import java.text.DecimalFormat
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test

class FormatCurrencyTest {

    @Test
    fun `aa`() {
        val f = FormatCurrency()
        var r = f.format("150.0")
        assertThat(r, `is`(FormatUtil.applyCurrency(150.0, DecimalFormat("#0.00"))))

        r = f.format("1500.0")
        assertThat(r, `is`(FormatUtil.applyCurrency(1500.0, DecimalFormat("#0.00"))))

        r = f.format("1500.30")
        assertThat(r, `is`(FormatUtil.applyCurrency(1500.30, DecimalFormat("#0.00"))))

        r = f.format("1500.330")
        assertThat(r, `is`(FormatUtil.applyCurrency(1500.330, DecimalFormat("#0.000"))))

        r = f.format("1501.330")
        assertThat(r, `is`(FormatUtil.applyCurrency(1501.330, DecimalFormat("#0.000"))))

        r = f.format("151.330")
        assertThat(r, `is`(FormatUtil.applyCurrency(151.330, DecimalFormat("#0.000"))))

        r = f.format("15.330")
        assertThat(r, `is`(FormatUtil.applyCurrency(15.330, DecimalFormat("#0.000"))))

        r = f.format("1.330")
        assertThat(r, `is`(FormatUtil.applyCurrency(1.330, DecimalFormat("#0.000"))))

        r = f.format(".330")
        assertThat(r, `is`(FormatUtil.applyCurrency(.330, DecimalFormat("#0.000"))))

        r = f.format(".33")
        assertThat(r, `is`(FormatUtil.applyCurrency(.33, DecimalFormat("#0.00"))))
    }

    @Test
    fun `bb`() {
        val f = FormatCurrency()
        var r = f.format("R$ 150.000,1890")
        assertThat(r, `is`(FormatUtil.applyCurrency(150000.1890, DecimalFormat("#0.0000"))))

        r = f.format("R$ 150.000,189")
        assertThat(r, `is`(FormatUtil.applyCurrency(150000.189, DecimalFormat("#0.000"))))

        r = f.format("R$ 150.00,189")
        assertThat(r, `is`(FormatUtil.applyCurrency(15000.189, DecimalFormat("#0.000"))))

        r = f.format("R$ 1500,189")
        assertThat(r, `is`(FormatUtil.applyCurrency(1500.189, DecimalFormat("#0.000"))))

        r = f.format("R$ 1500,19")
        assertThat(r, `is`(FormatUtil.applyCurrency(1500.19, DecimalFormat("#0.00"))))

        r = f.format("R$ 1500,199")
        assertThat(r, `is`(FormatUtil.applyCurrency(1500.199, DecimalFormat("#0.000"))))

        r = f.format("R$ 1.892.500,199848")
        assertThat(r, `is`(FormatUtil.applyCurrency(1892500.199848, DecimalFormat("#0.000000"))))
    }
}
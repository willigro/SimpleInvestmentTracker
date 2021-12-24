package com.rittmann.investmenttracks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rittmann.crypto.keep.ui.RegisterCryptoMovementActivity
import com.rittmann.crypto.list.ui.ListCryptoMovementsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ListCryptoMovementsActivity.start(this)
    }
}
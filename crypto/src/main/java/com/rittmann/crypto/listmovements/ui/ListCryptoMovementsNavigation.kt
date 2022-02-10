package com.rittmann.crypto.listmovements.ui

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.crypto.keep.ui.RegisterCryptoMovementActivity
import com.rittmann.deposit.keep.KeepDepositActivity

interface ListCryptoMovementsNavigation {
    fun close()
    fun goToRegisterNewCrypto()
    fun goToRegisterNewDeposit()
    fun goToUpdate(tradeMovement: TradeMovement)
    fun setStartResult(content: ActivityResultLauncher<Intent>)
}

class ListCryptoMovementsNavigationImpl(
    private val fragment: ListCryptoMovementsFragment
) : ListCryptoMovementsNavigation {


    private lateinit var content: ActivityResultLauncher<Intent>

    companion object {
        var close = true
    }

    override fun close() {
        if (close)
            fragment.requireActivity().finish()
    }

    override fun goToRegisterNewCrypto() {
        content.launch(RegisterCryptoMovementActivity.getIntent(fragment.requireContext()))
//        RegisterCryptoMovementActivity.start(fragment.requireContext())
    }

    override fun goToRegisterNewDeposit() {
        content.launch(KeepDepositActivity.getIntent(fragment.requireContext()))
    }

    override fun goToUpdate(tradeMovement: TradeMovement) {
        if (tradeMovement.type == CryptoOperationType.DEPOSIT)
            content.launch(KeepDepositActivity.getIntent(fragment.requireContext(), tradeMovement))
        else
            content.launch(RegisterCryptoMovementActivity.getIntent(fragment.requireContext(), tradeMovement))
    }

    override fun setStartResult(content: ActivityResultLauncher<Intent>) {
        this.content = content
    }
}
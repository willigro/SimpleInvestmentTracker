package com.rittmann.crypto.listmovements.ui

import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.crypto.keep.ui.RegisterCryptoMovementActivity

interface ListCryptoMovementsNavigation {
    fun close()
    fun goToRegisterNewCrypto()
    fun goToUpdateCrypto(cryptoMovement: CryptoMovement)
}

class ListCryptoMovementsNavigationImpl(
    private val fragment: ListCryptoMovementsFragment
) : ListCryptoMovementsNavigation {

    companion object {
        var close = true
    }

    override fun close() {
        if (close)
            fragment.requireActivity().finish()
    }

    override fun goToRegisterNewCrypto() {
        RegisterCryptoMovementActivity.start(fragment.requireContext())
    }

    override fun goToUpdateCrypto(cryptoMovement: CryptoMovement) {
        RegisterCryptoMovementActivity.start(fragment.requireContext(), cryptoMovement)
    }
}
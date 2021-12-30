package com.rittmann.crypto.list.ui

import com.rittmann.crypto.keep.ui.RegisterCryptoMovementActivity

interface ListCryptoMovementsNavigation {
    fun close()
    fun goToRegisterNewCrypto()
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
}
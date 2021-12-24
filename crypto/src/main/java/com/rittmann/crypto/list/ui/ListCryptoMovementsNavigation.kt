package com.rittmann.crypto.list.ui

import com.rittmann.crypto.keep.ui.RegisterCryptoMovementActivity

interface ListCryptoMovementsNavigation {
    fun close()
    fun goToRegisterNewCrypto()
}

class ListCryptoMovementsNavigationImpl(
    private val activity: ListCryptoMovementsActivity
) : ListCryptoMovementsNavigation {

    companion object {
        var close = true
    }

    override fun close() {
        if (close)
            activity.finish()
    }

    override fun goToRegisterNewCrypto() {
        RegisterCryptoMovementActivity.start(activity)
    }
}
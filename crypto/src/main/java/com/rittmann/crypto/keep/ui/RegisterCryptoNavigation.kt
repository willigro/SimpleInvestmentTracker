package com.rittmann.crypto.keep.ui

interface RegisterCryptoNavigation {
    fun close()
}

class RegisterCryptoNavigationImpl(
    private val activity: RegisterCryptoMovementActivity
) : RegisterCryptoNavigation {

    companion object {
        var close = true
    }

    override fun close() {
        if (close)
            activity.finish()
    }
}
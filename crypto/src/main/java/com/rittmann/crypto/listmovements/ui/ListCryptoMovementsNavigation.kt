package com.rittmann.crypto.listmovements.ui

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.crypto.keep.ui.RegisterCryptoMovementActivity

interface ListCryptoMovementsNavigation {
    fun close()
    fun goToRegisterNewCrypto()
    fun goToUpdateCrypto(cryptoMovement: CryptoMovement)
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

    override fun goToUpdateCrypto(cryptoMovement: CryptoMovement) {
        content.launch(RegisterCryptoMovementActivity.getIntent(fragment.requireContext(), cryptoMovement))

//        RegisterCryptoMovementActivity.start(fragment.requireContext(), cryptoMovement)
    }

    override fun setStartResult(content: ActivityResultLauncher<Intent>) {
        this.content = content
    }
}
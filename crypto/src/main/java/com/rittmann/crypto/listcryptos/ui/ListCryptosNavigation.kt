package com.rittmann.crypto.listcryptos.ui

import com.rittmann.crypto.results.ui.CryptoResultsActivity

interface ListCryptosNavigation {
    fun close()
    fun openCryptoResults(crypto: String)
}

class ListCryptosNavigationImpl(
    private val fragment: ListCryptosFragment
) : ListCryptosNavigation {

    companion object {
        var close = true
    }

    override fun close() {
        if (close)
            fragment.requireActivity().finish()
    }

    override fun openCryptoResults(crypto: String) {
        CryptoResultsActivity.start(fragment.requireContext(), crypto)
    }
}
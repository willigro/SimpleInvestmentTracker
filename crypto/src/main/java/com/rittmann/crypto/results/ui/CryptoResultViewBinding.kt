package com.rittmann.crypto.results.ui

import androidx.lifecycle.MutableLiveData

class CryptoResultViewBinding(
    val totalOnHand: MutableLiveData<String> = MutableLiveData(),
    val totalOnHandWithoutTax: MutableLiveData<String> = MutableLiveData(),
    val totalEarned: MutableLiveData<String> = MutableLiveData(),
    val totalInvested: MutableLiveData<String> = MutableLiveData(),
    val totalBoughtAmount: MutableLiveData<String> = MutableLiveData(),
    val totalSoldAmount: MutableLiveData<String> = MutableLiveData(),
    val totalOnHandAmount: MutableLiveData<String> = MutableLiveData(),
    val totalTaxPaid: MutableLiveData<String> = MutableLiveData(),
)
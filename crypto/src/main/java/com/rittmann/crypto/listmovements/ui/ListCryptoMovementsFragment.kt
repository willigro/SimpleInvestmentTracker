package com.rittmann.crypto.listmovements.ui

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.extensions.isOk
import com.rittmann.common.extensions.linearLayoutManager
import com.rittmann.common.lifecycle.BaseFragmentBinding
import com.rittmann.common.viewmodel.viewModelProvider
import com.rittmann.crypto.R
import com.rittmann.crypto.databinding.FragmentListCryptoMovementsBinding
import com.rittmann.widgets.dialog.modal
import javax.inject.Inject

class ListCryptoMovementsFragment : BaseFragmentBinding<FragmentListCryptoMovementsBinding>(
    R.layout.fragment_list_crypto_movements,
    R.id.container
) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var listCryptoMovementsNavigation: ListCryptoMovementsNavigation

    @VisibleForTesting
    lateinit var viewModel: ListCryptoMovementsViewModel

    private var adapter: RecyclerAdapterCryptoMovement? = null

    val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.isOk()) {
                viewModel.fetchAllCryptoMovements()
            }
        }

    private var mDialog: Dialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)
        binding.viewModel = viewModel

        initViews()
        initObservers()

        viewModel.fetchAllCryptoMovements()

        listCryptoMovementsNavigation.setStartResult(getContent)
    }

    private fun initViews() {
        binding.apply {
            buttonRegisterNewCrypto.setOnClickListener {
                if (mDialog == null) {
                    val mView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.content_bottom_sheet_keep_movement, null, false)
                    mDialog = createBootomSheet(
                        mView
                    )
                    mView?.findViewById<View>(R.id.content_bottom_sheet_keep_movement)
                        ?.setBackgroundColor(Color.TRANSPARENT)

                    mView?.findViewById<View>(R.id.txt_open_keep_deposit)?.setOnClickListener {
                        listCryptoMovementsNavigation.goToRegisterNewDeposit()
                    }

                    mView?.findViewById<View>(R.id.txt_open_keep_crypto)?.setOnClickListener {
                        listCryptoMovementsNavigation.goToRegisterNewCrypto()
                    }
                }

                mDialog?.show()
            }
        }
    }

    private fun initObservers() {
        viewModel.apply {
            tradeMovementsList.observe(this@ListCryptoMovementsFragment, { result ->
                when (result) {
                    is ResultEvent.Success -> {
                        binding.apply {
                            if (adapter == null) {
                                adapter = RecyclerAdapterCryptoMovement(
                                    result.data,
                                    listCryptoMovementsNavigation
                                ) { cryptoMovementToDelete ->
                                    this@ListCryptoMovementsFragment.viewModel.deleteCrypto(
                                        cryptoMovementToDelete
                                    )
                                }

                                recyclerCryptoMovement.linearLayoutManager()
                                recyclerCryptoMovement.adapter = adapter
                            } else {
                                adapter?.submitList(result.data.toMutableList())
                            }
                        }
                    }
                    else -> {
                        // TODO: error message
                        modal(
                            message = getString(R.string.list_crypto_error),
                            show = true,
                            ok = true,
                            cancelable = true
                        )
                    }
                }
            })

            cryptoMovementDeleted.observe(this@ListCryptoMovementsFragment, { result ->
                when (result) {
                    is ResultEvent.Success -> {
                        // TODO: to it right, man, late I'm going to change it for the right remove
                        //  remove the item from the list, recalculate and etc..

                        this@ListCryptoMovementsFragment.viewModel.fetchAllCryptoMovements()
                    }
                    else -> {
                        // TODO: error message
                        modal(
                            message = getString(R.string.list_crypto_error),
                            show = true,
                            ok = true,
                            cancelable = true
                        )
                    }
                }
            })

            observeProgress(this)
        }
    }

    private fun createBootomSheet(view: View?): Dialog? {
        if (view == null) throw NullPointerException("Não é possível criar um Bottom Sheet Dialog com uma view null, passe uma view válida para continuar.")
        if (view.context == null) throw NullPointerException("Não é possível criar um Bottom Sheet Dialog com um context null, passe uma view com context válido para continuar.")
        val dialog: Dialog = BottomSheetDialog(view.context)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        return dialog
    }
}
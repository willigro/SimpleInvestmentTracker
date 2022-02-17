package com.rittmann.crypto.listmovements.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.extensions.isOk
import com.rittmann.common.extensions.linearLayoutManager
import com.rittmann.common.lifecycle.BaseFragmentBinding
import com.rittmann.common.utils.pagination.PagingUtils
import com.rittmann.common.viewmodel.viewModelProvider
import com.rittmann.crypto.R
import com.rittmann.crypto.databinding.FragmentListCryptoMovementsBinding
import com.rittmann.crypto.keep.ui.RegisterCryptoMovementActivity.Companion.CRYPTO_MOVEMENT_RESULT_INSERTED
import com.rittmann.crypto.keep.ui.RegisterCryptoMovementActivity.Companion.CRYPTO_MOVEMENT_RESULT_UPDATED
import com.rittmann.deposit.keep.KeepDepositActivity.Companion.DEPOSIT_MOVEMENT_RESULT_INSERTED
import com.rittmann.deposit.keep.KeepDepositActivity.Companion.DEPOSIT_MOVEMENT_RESULT_UPDATED
import com.rittmann.widgets.dialog.ModalInternal
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

    private val tradeFilter: TradeFilter = TradeFilter()

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.isOk()) {
                result.data?.extras?.apply {
                    getSerializable(CRYPTO_MOVEMENT_RESULT_UPDATED)?.also { data ->
                        viewModel.tradeMovementWasUpdated(data as TradeMovement)
                    }

                    getSerializable(DEPOSIT_MOVEMENT_RESULT_UPDATED)?.also { data ->
                        viewModel.tradeMovementWasUpdated(data as TradeMovement)
                    }

                    getSerializable(CRYPTO_MOVEMENT_RESULT_INSERTED)?.also { data ->
                        viewModel.tradeMovementWasInserted(data as TradeMovement)
                    }

                    getSerializable(DEPOSIT_MOVEMENT_RESULT_INSERTED)?.also { data ->
                        viewModel.tradeMovementWasInserted(data as TradeMovement)
                    }
                }
            }
        }

    private var mDialog: Dialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)
        binding.viewModel = viewModel

        initViews()
        initObservers()

        fetchTradeMovements()

        listCryptoMovementsNavigation.setStartResult(getContent)
    }

    @SuppressLint("InflateParams")
    private fun initViews() {
        configureToolbar(getString(R.string.trade_movement_list_screen_name)) {
            viewModel.fetchAllCryptoMovementsName()
        }

        binding.apply {
            buttonRegisterNewCrypto.setOnClickListener {
                if (mDialog == null) {
                    val mView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.content_bottom_sheet_keep_movement, null, false)
                    mDialog = createBottomSheet(
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

            PagingUtils.setUpPaging(
                scroll = recyclerCryptoMovement,
                enableRefresh = this@ListCryptoMovementsFragment.viewModel.pageInfo.enableRefresh,
                refreshList = ::fetchTradeMovementsWithPaging
            )
        }
    }

    private fun initObservers() {
        viewModel.apply {
            tradeMovementsList.observe(this@ListCryptoMovementsFragment, { result ->
                binding.apply {
                    if (adapter == null) {
                        adapter = RecyclerAdapterCryptoMovement(
                            result,
                            listCryptoMovementsNavigation,
                            ::deleteTradeMovement
                        )

                        recyclerCryptoMovement.linearLayoutManager()
                        recyclerCryptoMovement.adapter = adapter
                    } else {
                        adapter?.submitList(result.toMutableList())
                    }
                }
            })

            cryptoMovementDeleted.observe(this@ListCryptoMovementsFragment, { result ->
                val msg = when (result) {
                    is ResultEvent.Success -> {
                        getString(R.string.trade_movement_delete_was_successful)
                    }
                    else -> {
                        getString(R.string.trade_movement_delete_was_not_successful)
                    }
                }
                modal(
                    message = msg,
                    show = true,
                    ok = true,
                    cancelable = true
                )
            })

            tradeNameList.observe(viewLifecycleOwner, { result ->
                when (result) {
                    is ResultEvent.Success -> {
                        showFilterModal(result.data)
                    }
                    else -> {
                        modal(
                            message = getString(R.string.trade_movement_list_filter_error_while_trying_to_get_the_names),
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

    @SuppressLint("InflateParams")
    private fun showFilterModal(data: List<String>) {
        modal(
            ModalInternal(
                context = requireActivity(),
                resIdLayout = R.layout.dialog_trade_filters,
                title = getString(R.string.trade_movement_list_filter_title),
                message = getString(R.string.trade_movement_list_filter_message),
                show = true,
                cancelable = true,
                onClickConclude = {
                    tradeFilter.nameFilters.clear()
                    tradeFilter.nameFilters.addAll(tradeFilter.tempNameFilters)
                    fetchTradeMovements(resetPaging = true)
                }
            )
        ).apply {
            val linear = dialogView.findViewById<LinearLayoutCompat>(R.id.list_filter_item_by_name)

            for (name in data) {
                val view = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_trade_filter_names_adapter, null, false)
                val textView = view.findViewById<TextView>(R.id.txt_dialog_trade_filter_name)
                textView.text = name
                linear.addView(view)

                if (tradeFilter.nameFilters.contains(name))
                    textView.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.robbie_ripple_color
                        )
                    )
                else
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

                tradeFilter.tempNameFilters.clear()
                tradeFilter.tempNameFilters.addAll(tradeFilter.nameFilters)

                view.setOnClickListener {
                    if (tradeFilter.tempNameFilters.contains(name)) {
                        tradeFilter.tempNameFilters.remove(name)
                        textView.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )
                    } else {
                        tradeFilter.tempNameFilters.add(name)
                        textView.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.robbie_ripple_color
                            )
                        )
                    }
                }
            }
        }
    }

    private fun deleteTradeMovement(trade: TradeMovement) {
        modal(
            message = getString(R.string.do_you_wanna_delete_this_trade),
            show = true,
            cancelable = true,
            onClickConclude = {
                viewModel.deleteCrypto(
                    trade
                )
            }
        )
    }

    private fun fetchTradeMovementsWithPaging() {
        fetchTradeMovements(true)
    }

    private fun fetchTradeMovements(
        next: Boolean = false,
        resetPaging: Boolean = false
    ) {
        viewModel.fetchAllCryptoMovements(next, resetPaging, tradeFilter)
    }

    private fun createBottomSheet(view: View?): Dialog {
        if (view == null) throw NullPointerException("Não é possível criar um Bottom Sheet Dialog com uma view null, passe uma view válida para continuar.")
        if (view.context == null) throw NullPointerException("Não é possível criar um Bottom Sheet Dialog com um context null, passe uma view com context válido para continuar.")
        val dialog: Dialog = BottomSheetDialog(view.context)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        return dialog
    }
}

class TradeFilter(
    val nameFilters: ArrayList<String> = arrayListOf(),
    val tempNameFilters: ArrayList<String> = arrayListOf()
)
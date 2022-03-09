package com.rittmann.crypto.listmovements.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModelProvider
import com.rittmann.common.customs.views.createBottomSheet
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
        configureToolbar {
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
//                    mView?.findViewById<View>(R.id.content_bottom_sheet_keep_movement)
//                        ?.setBackgroundColor(Color.TRANSPARENT)

                    mView?.findViewById<View>(R.id.view_open_keep_deposit)?.setOnClickListener {
                        listCryptoMovementsNavigation.goToRegisterNewDeposit()
                    }

                    mView?.findViewById<View>(R.id.view_open_keep_crypto)?.setOnClickListener {
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

    private fun showFilterModal(data: List<String>) {
        BottomSheetFilters.createBottomSheetFilter(requireContext(), data, tradeFilter) {
            fetchTradeMovements(resetPaging = true)
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
}

class TradeFilter(
    val nameFilters: ArrayList<String> = arrayListOf(),
    val tempNameFilters: ArrayList<String> = arrayListOf()
)
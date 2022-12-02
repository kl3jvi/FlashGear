package com.kl3jvi.yonda.ui.scanner

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.kl3jvi.yonda.connectivity.ConnectionService
import com.kl3jvi.yonda.databinding.ScanRecyclerBinding
import com.kl3jvi.yonda.ext.launchAndRepeatWithViewLifecycle
import com.kl3jvi.yonda.ext.showToast
import com.kl3jvi.yonda.ui.home.BluetoothState
import com.kl3jvi.yonda.ui.home.HomeViewModel
import com.kl3jvi.yonda.ui.home.ScanResultsAdapter
import com.maxkeppeler.sheets.core.Sheet
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias PositiveListener = () -> Unit

class ScanBottomSheet : Sheet(), KoinComponent {
    private lateinit var adapter: ScanResultsAdapter
    override val dialogTag = "CustomSheet"
    private lateinit var binding: ScanRecyclerBinding

    private val homeViewModel: HomeViewModel by viewModel()
    private val connectionService: ConnectionService by inject()

    /**
     * Implement this method and add your own layout, which will be appended to the default sheet with toolbar and buttons.
     */
    override fun onCreateLayoutView(): View {
        return ScanRecyclerBinding.inflate(LayoutInflater.from(activity)).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ScanResultsAdapter(homeViewModel)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter


        launchAndRepeatWithViewLifecycle { homeViewModel.scannedDeviceList.collect(::showViewOnState) }
        setButtonPositiveListener { homeViewModel.stopScanPressed() }
//        setButtonPositiveListener {  } If you want to override the default positive click listener
//        displayButtonsView() If you want to change the visibility of the buttons view
//        displayButtonPositive() Hiding the positive button will prevent clicks
//        hideToolbar() Hide the toolbar of the sheet, the title and the icon
    }

    private fun showViewOnState(bluetoothState: BluetoothState) {
        when (bluetoothState) {
            is BluetoothState.Error -> requireContext()
                .showToast(bluetoothState.errorMessage)

            is BluetoothState.Success -> {
                binding.rv.isVisible = true
                adapter.submitList(bluetoothState.data)
            }

            else -> {}
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        homeViewModel.stopScanPressed()
    }


    /** Build [CustomSheet] and show it later. */
    fun build(ctx: Context, width: Int? = null, func: ScanBottomSheet.() -> Unit): ScanBottomSheet {
        this.windowContext = ctx
        this.width = width
        this.func()
        return this
    }

    /** Build and show [CustomSheet] directly. */
    fun show(
        ctx: Context,
        width: Int? = null,
        func: ScanBottomSheet.() -> Unit,
    ): ScanBottomSheet {
        this.windowContext = ctx
        this.width = width
        this.func()
        this.show()
        this.displayNegativeButton(false)
        return this
    }

    fun onPositive(positiveListener: PositiveListener) {
        this.positiveListener = positiveListener
    }

    fun onPositive(@StringRes positiveRes: Int, positiveListener: PositiveListener? = null) {
        this.positiveText = windowContext.getString(positiveRes)
        this.positiveListener = positiveListener
    }

    fun onPositive(positiveText: String, positiveListener: PositiveListener? = null) {
        this.positiveText = positiveText
        this.positiveListener = positiveListener
    }
}
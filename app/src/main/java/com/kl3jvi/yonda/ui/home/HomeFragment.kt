package com.kl3jvi.yonda.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kl3jvi.yonda.R
import com.kl3jvi.yonda.connectivity.ConnectionService
import com.kl3jvi.yonda.databinding.FragmentHomeBinding
import com.kl3jvi.yonda.ui.scanner.ScanBottomSheet
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeFragment : Fragment(R.layout.fragment_home), KoinComponent {
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()
    private val connectionService: ConnectionService by inject()

    private lateinit var resultsAdapter: ScanResultsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
//        resultsAdapter = ScanResultsAdapter()
//        binding.rv.layoutManager = LinearLayoutManager(requireContext())
//        binding.rv.adapter = resultsAdapter
        binding.fab.setOnClickListener {
            if (!connectionService.isBluetoothEnabled()) {
                askToTurnOnBluetooth()
                return@setOnClickListener
            }
            scanBle()
        }
    }


    private fun scanBle() {
        ScanBottomSheet().show(requireContext()) {
            title("Scanning List")
            behavior(BottomSheetBehavior.STATE_EXPANDED)
        }

//        binding.isScanning = isScanning
//        requireContext().showToastIf(
//            "Started Scanning",
//            "Stopped Scanning"
//        ) { isScanning }
//
//        if (!isScanning) {
//            homeViewModel.stopScanPressed()
//        } else {
//            launchAndRepeatWithViewLifecycle {
////                homeViewModel.scannedDeviceList.collect(::showViewOnState)
//            }
//        }
    }


    private fun askToTurnOnBluetooth() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_title))
            .setMessage(getString(R.string.usage_message))
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
                val intentOpenBluetoothSettings = Intent()
                intentOpenBluetoothSettings.action = Settings.ACTION_BLUETOOTH_SETTINGS
                startActivity(intentOpenBluetoothSettings)
            }
            .setCancelable(false)
            .show()
    }

    /**
     * > Show the progress bar when the state is idle, show the recycler view when the state is
     * success, and show a toast when the state is error
     *
     * @param bluetoothState This is the state of the bluetooth. It can be in one of the following
     * states:
     */
//    private fun showViewOnState(bluetoothState: BluetoothState) {
//        binding.apply {
//            binding.rv.isVisible = bluetoothState is BluetoothState.Success
//            binding.progress.isVisible = bluetoothState is BluetoothState.Idle
//        }
//        when (bluetoothState) {
//            is BluetoothState.Error -> requireContext()
//                .showToast(bluetoothState.errorMessage)
//
//            is BluetoothState.Success -> {
//                binding.progress.isVisible = false
//                binding.rv.isVisible = true
//                Log.e("Results", bluetoothState.data.toString())
//                resultsAdapter.submitList(bluetoothState.data)
//
//                ScanBottomSheet().show(resultsAdapter,bluetoothState.data, requireContext()) {
//                    title("Scan List")
//
//                }
//            }
//
//            else -> {}
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

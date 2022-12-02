package com.kl3jvi.yonda.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kl3jvi.yonda.MainActivity
import com.kl3jvi.yonda.R
import com.kl3jvi.yonda.databinding.FragmentHomeBinding
import com.kl3jvi.yonda.ext.launchAndRepeatWithViewLifecycle
import com.kl3jvi.yonda.ext.showToast
import com.kl3jvi.yonda.ext.showToastIf
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()

    private lateinit var resultsAdapter: ScanResultsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        resultsAdapter = ScanResultsAdapter(homeViewModel)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = resultsAdapter
    }

    override fun onResume() {
        super.onResume()
        setupScanButton()
    }

    private fun setupScanButton() {
        (activity as? MainActivity)?.scanBle { isScanning ->
            requireContext().showToastIf(
                "Started Scanning",
                "Stopped Scanning"
            ) { isScanning }

            if (!isScanning) {
                homeViewModel.stopScanPressed()
            } else {
                launchAndRepeatWithViewLifecycle {
                    homeViewModel.scannedDeviceList
                        .collect(::showViewOnState)
                }
                launchAndRepeatWithViewLifecycle {
                    homeViewModel.currentConnectState.collect {
                        Log.e("Current ${it.first?.name} state:", it.second?.name.toString())
                    }
                }
            }
        }
    }

    /**
     * > Show the progress bar when the state is idle, show the recycler view when the state is
     * success, and show a toast when the state is error
     *
     * @param bluetoothState This is the state of the bluetooth. It can be in one of the following
     * states:
     */
    private fun showViewOnState(bluetoothState: BluetoothState) {
        binding.apply {
            binding.rv.isVisible = bluetoothState is BluetoothState.Success
            binding.progress.isVisible = bluetoothState is BluetoothState.Idle
        }
        when (bluetoothState) {
            is BluetoothState.Error -> requireContext()
                .showToast(bluetoothState.errorMessage)

            is BluetoothState.Success -> {
                binding.progress.isVisible = false
                binding.rv.isVisible = true
                Log.e("Results", bluetoothState.data.toString())
                resultsAdapter.submitList(bluetoothState.data)
            }

            else -> {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

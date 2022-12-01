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
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.collectLatest
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
            homeViewModel.scanStopped = !isScanning
            if (!isScanning) {
                homeViewModel.stopScanPressed()
            } else {
                launchAndRepeatWithViewLifecycle {
                    homeViewModel.connectedDevices
                        .collect { bluetoothState ->
                            Log.e("Activity ", this.ensureActive().toString())
                            when (bluetoothState) {
                                is BluetoothState.Error -> requireContext()
                                    .showToast(bluetoothState.errorMessage)

                                BluetoothState.Idle -> {
                                    binding.progress.isVisible = true
                                    binding.rv.isVisible = false
                                }

                                is BluetoothState.Success -> {
                                    binding.progress.isVisible = false
                                    binding.rv.isVisible = true
                                    Log.e(
                                        "State",
                                        bluetoothState.data.map { it.peripheral.address }.toString()
                                    )
                                    resultsAdapter.submitList(bluetoothState.data.sortedByDescending { it.peripheral.name })
                                }
                            }
                        }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

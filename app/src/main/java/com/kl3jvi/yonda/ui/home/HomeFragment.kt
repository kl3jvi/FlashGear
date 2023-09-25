package com.kl3jvi.yonda.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kl3jvi.yonda.R
import com.kl3jvi.yonda.databinding.FragmentHomeBinding
import com.kl3jvi.yonda.ext.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class HomeFragment : Fragment(R.layout.fragment_home), KoinComponent {

    private lateinit var adapter: ScanResultsAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()
    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        adapter = ScanResultsAdapter(homeViewModel)

        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
        scanBle(true)
    }

    private fun scanBle(isScanning: Boolean) {
        if (!isScanning) {
            job?.cancel()
            homeViewModel.stopScanPressed()
            homeViewModel.checkForAnimation.update { false }
        } else {
            job?.cancel()
            job = launchAndRepeatWithViewLifecycle {
                homeViewModel.scannedDeviceList.collect { bluetoothState ->
                    when (bluetoothState) {
                        is BluetoothState.Error -> Log.e(
                            "Error",
                           "happened ${bluetoothState.errorMessage}",
                        )

                        is BluetoothState.Success -> {
                            adapter.submitList(bluetoothState.data.sortedBy { it.address })
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job = null
        _binding = null
    }
}

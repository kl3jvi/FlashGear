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
        scanBle()
//        setup floating button action when clicked it either starts the search or stop it
        binding.fab.setOnClickListener {
            if (binding.fab.tag == "start") {
                binding.fab.tag = "stop"
                binding.fab.setImageResource(R.drawable.ic_stop_search)
                homeViewModel.connectionService.startScanning()
                scanBle()
            } else {
                binding.fab.tag = "start"
                binding.fab.setImageResource(R.drawable.ic_search)
                job?.cancel()
            }
        }
    }

    private fun scanBle() {
        job?.cancel()
        job = launchAndRepeatWithViewLifecycle {
            homeViewModel.scannedDeviceList.collect { bluetoothState ->
                when (bluetoothState) {
                    is BluetoothState.Error -> Log.e(
                        "Error",
                        "happened ${bluetoothState.errorMessage}",
                    )

                    is BluetoothState.Success -> {
                        Log.e("Success", "happened ${bluetoothState.data}")
                        adapter.submitList(bluetoothState.data.sortedBy { it.device?.name == null })
                    }

                    else -> {}
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

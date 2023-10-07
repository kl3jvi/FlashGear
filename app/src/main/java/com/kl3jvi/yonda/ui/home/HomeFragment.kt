package com.kl3jvi.yonda.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kl3jvi.yonda.R
import com.kl3jvi.yonda.databinding.FragmentHomeBinding
import com.kl3jvi.yonda.ext.launchAndRepeatWithViewLifecycle
import com.kl3jvi.yonda.manager.state.ConnectionState
import com.kl3jvi.yonda.manager.state.getStateAsString
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import java.util.concurrent.CancellationException

class HomeFragment : Fragment(R.layout.fragment_home), KoinComponent {

    private lateinit var adapter: ScanResultsAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()
    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        initRecyclerView()
        observeCommands()
        observeConnectivityState()
        setupFab()
    }

    private fun initRecyclerView() {
        adapter = ScanResultsAdapter(homeViewModel)
        binding.rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
        }
    }

    private fun observeCommands() {
        homeViewModel.commands()
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach(::processCommand)
            .launchIn(lifecycleScope)
    }

    private fun observeConnectivityState() {
        homeViewModel.currentConnectivityState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                processConnectionState(it)
                showSnackbar(it.getStateAsString())
            }.launchIn(lifecycleScope)
    }

    private fun processConnectionState(connectionState: ConnectionState) {
        when (connectionState) {
            is ConnectionState.Ready -> homeViewModel.sendCommand(ScanCommand.Stop)
            else -> {}
        }
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            when (it.tag) {
                "stopped" -> {
                    homeViewModel.sendCommand(ScanCommand.Start)
                    updateFabStatus("started", R.drawable.ic_stop_search)
                    updateProgressVisibility(true)
                }

                "started" -> {
                    homeViewModel.sendCommand(ScanCommand.Stop)
                    updateFabStatus("stopped", R.drawable.ic_search)
                    updateProgressVisibility(false)
                }
            }
        }
    }

    private fun updateFabStatus(tag: String, iconRes: Int) {
        binding.apply {
            this.fab.tag = tag
            this.fab.setImageResource(iconRes)
        }
    }

    private fun updateProgressVisibility(isVisible: Boolean) {
        binding.progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun processCommand(scanCommand: ScanCommand) {
        when (scanCommand) {
            is ScanCommand.Start -> {
                homeViewModel.startScanning()
                collectBleDevices()
                showSnackbar("Scanning started")
                binding.fab.tag = "started"
            }

            is ScanCommand.Stop -> {
                homeViewModel.stopScanning()
                job?.cancel()
                showSnackbar("Scanning stopped")
                binding.fab.tag = "stopped"
            }
        }
    }

    private fun collectBleDevices() {
        job?.cancel(CancellationException("Restarting scan"))
        job = launchAndRepeatWithViewLifecycle {
            homeViewModel.scannedDeviceList.collect { bluetoothState ->
                handleBluetoothState(bluetoothState)
            }
        }
    }

    private fun handleBluetoothState(bluetoothState: BluetoothState) {
        when (bluetoothState) {
            is BluetoothState.Error -> Log.e("Error", "happened ${bluetoothState.errorMessage}")
            is BluetoothState.Success -> {
//                Log.e("Success", "happened ${bluetoothState.data.map { it.state to it.device?.name }}")
                adapter.submitList(bluetoothState.data.sortedBy { it.device?.name == null })
            }

            else -> {}
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job = null
        binding.fab.tag = "stopped"
        _binding = null
    }
}

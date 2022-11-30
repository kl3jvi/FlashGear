package com.kl3jvi.yonda.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kl3jvi.yonda.MainActivity
import com.kl3jvi.yonda.R
import com.kl3jvi.yonda.databinding.FragmentHomeBinding
import com.kl3jvi.yonda.ext.launchAndRepeatWithViewLifecycle
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()

    private val resultsAdapter: ScanResultsAdapter = ScanResultsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = resultsAdapter
    }

    override fun onResume() {
        super.onResume()
        setupScanButton()
    }

    private fun setupScanButton() {
        (activity as? MainActivity)?.scanBle { isScanning ->
            if (!isScanning) {
                homeViewModel.stopScanPressed()
                Snackbar.make(binding.root, "Stopped Scanning", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(
                    binding.root, "Started scanning for peripherals",
                    Snackbar.LENGTH_SHORT
                ).show()
                launchAndRepeatWithViewLifecycle {
                    homeViewModel.connectedDevices.collect { bluetoothState ->
                        when (bluetoothState) {
                            is BluetoothState.Error -> Toast.makeText(
                                requireContext(),
                                bluetoothState.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()

                            BluetoothState.Idle -> {
                                binding.progress.isVisible = true
//                            binding.rv.isVisible = false
                            }

                            is BluetoothState.Success -> {
                                binding.progress.isVisible = false
//                            binding.rv.isVisible = true
                                Log.e("State", bluetoothState.data.toString())
                                resultsAdapter.submitList(bluetoothState.data)
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

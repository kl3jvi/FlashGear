package com.kl3jvi.yonda.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.kl3jvi.yonda.R
import com.kl3jvi.yonda.databinding.FragmentHomeBinding
import com.kl3jvi.yonda.ext.launchAndRepeatWithViewLifecycle
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class HomeFragment : Fragment(R.layout.fragment_home), KoinComponent {

    private lateinit var adapter: ScanResultsAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        adapter = ScanResultsAdapter(homeViewModel)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter

        binding.fab.setOnClickListener {
            scanBle()
        }
//        launchAndRepeatWithViewLifecycle {
//            homeViewModel.isScanning.collect { isScanning ->
//                Log.e("isScanning",isScanning.toString())
//                binding.fab.isActivated = !isScanning
//                binding.lottieAnimationView.playAnimationIf(isScanning)
//                binding.isScanning = isScanning
////                if (isScanning) {
////                    homeViewModel.stopScanPressed()
////                } else {
//
////                }
//            }
//        }

    }

    private fun scanBle() {


        launchAndRepeatWithViewLifecycle {
            homeViewModel.scannedDeviceList.collect { bluetoothState ->
                when (bluetoothState) {
                    is BluetoothState.Error -> Log.e(
                        "Error",
                        "happened ${bluetoothState.errorMessage}"
                    )

                    BluetoothState.Idle -> {}

                    is BluetoothState.Success -> {
                        Log.e(
                            "Data",
                            bluetoothState.data.map { it.peripheral.address }.toString()
                        )
                        adapter.submitList(bluetoothState.data)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * > If the predicate is true, play the animation. Otherwise, cancel the animation, play it, and
     * then cancel it again
     *
     * @param predicate Boolean - This is the condition that will determine whether the animation will
     * play or not.
     */
    private fun LottieAnimationView.playAnimationIf(predicate: Boolean) {
        if (predicate) {
            playAnimation()
        } else {
            cancelAnimation()
            playAnimation()
            cancelAnimation()
        }
    }
}

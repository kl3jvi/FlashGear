package com.kl3jvi.yonda.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kl3jvi.yonda.MainActivity
import com.kl3jvi.yonda.R
import com.kl3jvi.yonda.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()

    private lateinit var resultsAdapter: ScanResultsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        resultsAdapter = ScanResultsAdapter()
        binding.rv.adapter = resultsAdapter
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onResume() {
        super.onResume()
        setupScanButton()
    }

    private fun setupScanButton() {
        (activity as? MainActivity)?.scanBle {
            homeViewModel.scan()
                .subscribe({ resultsAdapter.submitList(mutableListOf(it)) }, {
                    Log.e("Error", "happened", it)
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeViewModel.scan()
        _binding = null
    }
}

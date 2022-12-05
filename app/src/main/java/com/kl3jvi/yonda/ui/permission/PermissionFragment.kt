package com.kl3jvi.yonda.ui.permission

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kl3jvi.yonda.R
import com.kl3jvi.yonda.databinding.FragmentPermissionBinding
import com.kl3jvi.yonda.ext.enableBluetooth
import com.kl3jvi.yonda.ext.enableLocation
import com.kl3jvi.yonda.ext.isBluetoothGranted
import com.kl3jvi.yonda.ext.isLocationGranted

class PermissionFragment : Fragment(R.layout.fragment_permission) {

    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding!!

    /**
     * If the user has granted both the location and bluetooth permissions, then navigate to the
     * HomeFragment
     */
    override fun onStart() {
        super.onStart()
        if (isBluetoothGranted() && isLocationGranted()) {
            findNavController().navigate(
                PermissionFragmentDirections.toHome()
            )
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPermissionBinding.bind(view)
        binding.bluetooth.setOnClickListener { enableBluetooth() }
        binding.location.setOnClickListener { enableLocation() }
        binding.proceedButton.setOnClickListener {
            findNavController().navigate(
                PermissionFragmentDirections.toHome()
            )
        }
    }


    /**
     * If the user has granted both location and bluetooth permissions, then change the color of the
     * text and the drawable to teal
     */
    @SuppressLint("UseCompatTextViewDrawableApis")
    private fun TextView.checkShouldEnable() = apply {
        if (isBluetoothGranted() && isLocationGranted()) {
            setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_700))
            val color = ContextCompat.getColor(context, R.color.teal_700)
            val colorList = ColorStateList.valueOf(color)
            compoundDrawableTintList = colorList
        }
    }


    /**
     * "Check if the button should be enabled, and if so, enable it."
     *
     * The function is called on a button, and it checks if the button should be enabled. If it should,
     * it enables it
     */
    private fun Button.checkShouldEnable() = apply {
        isEnabled = isBluetoothGranted() && isLocationGranted()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        binding.apply {
            val permissionList = listOf(location, bluetooth)
            permissionList.checkTextViews()
            proceedButton.checkShouldEnable()
            allowState.text = if (isBluetoothGranted()) "Allowed" else "Allow here"
        }
    }

    private fun List<TextView>.checkTextViews() {
        forEach { it.checkShouldEnable() }
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

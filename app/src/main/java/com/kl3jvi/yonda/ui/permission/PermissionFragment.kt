package com.kl3jvi.yonda.ui.permission

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kl3jvi.yonda.R
import com.kl3jvi.yonda.databinding.FragmentPermissionBinding

class PermissionFragment : Fragment(R.layout.fragment_permission) {

    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding!!
    private val permissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            when (it.key) {
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH -> {
                    // Handle Bluetooth Permissions
                    if (it.value) {
                        binding.bluetooth.checkShouldEnable()
                    }
                }

                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION -> {
                    // Handle Location Permissions
                    if (it.value) {
                        binding.location.checkShouldEnable()
                    }
                }
            }
        }
        binding.proceedButton.checkShouldEnable()
    }

    /**
     * If the user has granted both the location and bluetooth permissions, then navigate to the
     * HomeFragment
     */
    override fun onStart() {
        super.onStart()

        if (!isBluetoothGranted() && !isLocationGranted()) {

            findNavController().navigate(
                PermissionFragmentDirections.toHome(),
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
                PermissionFragmentDirections.toHome(),
            )
        }
    }

    private fun enableBluetooth() {
        permissionRequestLauncher.launch(
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        )
    }

    private fun enableLocation() {
        permissionRequestLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    private fun isBluetoothGranted(): Boolean {
        val bluetoothPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.BLUETOOTH
        )
        val bluetoothAdminPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.BLUETOOTH_ADMIN
        )

        val bluetoothConnectPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.BLUETOOTH_CONNECT
        )
        return bluetoothPermission == PackageManager.PERMISSION_GRANTED &&
                bluetoothAdminPermission == PackageManager.PERMISSION_GRANTED &&
                bluetoothConnectPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * If the user has granted both location and bluetooth permissions, then change the color of the
     * text and the drawable to teal
     */
    @SuppressLint("UseCompatTextViewDrawableApis")
    private fun TextView.checkShouldEnable() {
        val context = requireContext()
        val color = ContextCompat.getColor(context, R.color.teal_700)
        val colorList = ColorStateList.valueOf(color)

        compoundDrawableTintList = if (isBluetoothGranted() && isLocationGranted()) {
            setTextColor(color)
            colorList
        } else if (!isBluetoothGranted() && isLocationGranted()) {
            setTextColor(ContextCompat.getColor(context, R.color.black))
            null
        } else if (isBluetoothGranted() && !isLocationGranted()) {
            setTextColor(color)
            colorList
        } else {
            setTextColor(ContextCompat.getColor(context, R.color.black))
            null
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

package com.kl3jvi.yonda

import android.Manifest.permission.BLUETOOTH_ADMIN
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kl3jvi.yonda.connectivity.ConnectionService
import com.kl3jvi.yonda.databinding.ActivityMainBinding
import com.kl3jvi.yonda.ext.checkSelfPermissions
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class MainActivity : AppCompatActivity(), KoinComponent {

    private val connectionService: ConnectionService by inject()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkSelfPermissions(
            BLUETOOTH_CONNECT,
            BLUETOOTH_SCAN,
            BLUETOOTH_ADMIN
        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNavigationView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.fab.isEnabled = connectionService.isBluetoothEnabled()

    }

    fun scanBle(block: (Boolean) -> Unit) {
        binding.fab.setOnClickListener {
            if (!connectionService.isBluetoothEnabled()) {
                askToTurnOnBlueTooth()
                return@setOnClickListener
            }
            binding.fab.isActivated = !binding.fab.isActivated
            block(binding.fab.isActivated)
        }
    }

    private fun askToTurnOnBlueTooth() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.dialog_title))
            .setMessage(getString(R.string.usage_message))
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
                val intentOpenBluetoothSettings = Intent()
                intentOpenBluetoothSettings.action = Settings.ACTION_BLUETOOTH_SETTINGS
                startActivity(intentOpenBluetoothSettings)
            }
            .setCancelable(false)
            .show()
    }
}

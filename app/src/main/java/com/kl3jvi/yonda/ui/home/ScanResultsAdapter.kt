package com.kl3jvi.yonda.ui.home

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kl3jvi.yonda.databinding.ItemBluetoothBinding
import com.kl3jvi.yonda.models.BleDevice
import com.welie.blessed.BluetoothPeripheral

interface ConnectListener {
    fun connectToPeripheral(peripheral: BluetoothPeripheral)
    fun sendCommandToPeripheral(peripheral: BluetoothPeripheral)
}

class ScanResultsAdapter(
    private val listener: ConnectListener,
) : ListAdapter<BluetoothDevice, ScanResultsAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<BluetoothDevice>() {
        override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BluetoothDevice,
            newItem: BluetoothDevice
        ): Boolean {
            return oldItem.address == newItem.address
        }
    },
) {

    inner class ViewHolder(private val binding: ItemBluetoothBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.connectionListener = listener
        }

        fun bind(bleDevice: BluetoothDevice) {
            binding.apply {
                this.bleDevice = bleDevice
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemBluetoothBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))
}

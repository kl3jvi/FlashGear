package com.kl3jvi.yonda.ui.home

import android.annotation.SuppressLint
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
) : ListAdapter<BleDevice, ScanResultsAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<BleDevice>() {
        override fun areItemsTheSame(oldItem: BleDevice, newItem: BleDevice): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: BleDevice, newItem: BleDevice): Boolean {
            return oldItem.peripheral == newItem.peripheral
        }
    }
) {

    inner class ViewHolder(private val binding: ItemBluetoothBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.connectionListener = listener
        }

        fun bind(bleDevice: BleDevice) {
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

package com.kl3jvi.yonda.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kl3jvi.yonda.databinding.ItemBluetoothBinding
import com.kl3jvi.yonda.models.ScanHolder

interface ConnectListener {
    fun connectToPeripheral(peripheral: ScanHolder)
    fun sendCommandToPeripheral(peripheral: ScanHolder)
}

class ScanResultsAdapter(
    private val listener: ConnectListener,
) : ListAdapter<ScanHolder, ScanResultsAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<ScanHolder>() {
        override fun areItemsTheSame(oldItem: ScanHolder, newItem: ScanHolder): Boolean {
            return oldItem.device?.address == newItem.device?.address
        }

        override fun areContentsTheSame(oldItem: ScanHolder, newItem: ScanHolder): Boolean {
            return oldItem == newItem
        }
    },
) {

    inner class ViewHolder(private val binding: ItemBluetoothBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.connectionListener = listener
        }

        fun bind(bleDevice: ScanHolder) {
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

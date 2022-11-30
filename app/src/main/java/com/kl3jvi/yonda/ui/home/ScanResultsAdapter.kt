package com.kl3jvi.yonda.ui.home

// internal class ScanResultsAdapter : ListAdapter<RxBleDevice, ScanResultsAdapter.ViewHolder>(
//    object : DiffUtil.ItemCallback<RxBleDevice>() {
//        override fun areItemsTheSame(oldItem: RxBleDevice, newItem: RxBleDevice): Boolean {
//            return oldItem == newItem
//        }
//
//        @SuppressLint("DiffUtilEquals")
//        override fun areContentsTheSame(oldItem: RxBleDevice, newItem: RxBleDevice): Boolean {
//            return oldItem.macAddress == newItem.macAddress
//        }
//    }
// ) {
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val device: TextView = itemView.findViewById(android.R.id.text1)
//        val rssi: TextView = itemView.findViewById(android.R.id.text2)
//    }
//
//
//    override fun getItemCount(): Int = currentList.size
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        with(currentList[position]) {
//            holder.device.text = String.format(
//                "%s (%s) %s",
//                macAddress,
//                name,
//                connectionState
//            )
//            holder.rssi.text = String.format("RSSI:", "e")
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
//        LayoutInflater.from(parent.context)
//            .inflate(android.R.layout.two_line_list_item, parent, false)
//            .let { ViewHolder(it) }
// }

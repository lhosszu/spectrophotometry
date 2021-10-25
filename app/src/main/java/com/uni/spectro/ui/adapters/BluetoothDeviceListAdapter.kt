package com.uni.spectro.ui.adapters

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uni.spectro.R
import com.uni.spectro.ui.adapters.BluetoothDeviceListAdapter.DeviceViewHolder

class BluetoothDeviceListAdapter(context: Context?, devices: MutableList<BluetoothDevice>) : RecyclerView.Adapter<DeviceViewHolder>() {

    private val items: MutableList<BluetoothDevice> = devices
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val itemView = inflater.inflate(R.layout.item_bluetooth_device, parent, false)
        return DeviceViewHolder(itemView, this)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val current = items[position]
        holder.deviceNameTextView.text = current.name
        holder.deviceMacTextView.text = current.address
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun add(item: BluetoothDevice) {
        items.add(item)
        notifyItemInserted(items.size)
    }

    fun addAll(newItems: Collection<BluetoothDevice>?) {
        items.clear()
        items.addAll(newItems!!)
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun getItem(index: Int): BluetoothDevice {
        return items[index]
    }

    inner class DeviceViewHolder(itemView: View, adapter: BluetoothDeviceListAdapter) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var deviceNameTextView: TextView = itemView.findViewById(R.id.text_device_name)
        var deviceMacTextView: TextView = itemView.findViewById(R.id.text_device_mac)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener.onItemClick(v, adapterPosition)
        }
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

}
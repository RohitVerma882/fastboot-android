package dev.rohitverma882.miunlock.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import dev.rohitverma882.miunlock.databinding.ItemFastbootDeviceBinding
import dev.rohitverma882.miunlock.ui.OnFastbootDeviceItemClickListener

class FastbootDeviceAdapter : OnFastbootDeviceItemClickListener,
    PagedListAdapter<FastbootDevice, FastbootDeviceAdapter.ViewHolder>(FASTBOOT_DEVICE_COMPARATOR) {
    private var listeners: MutableList<OnFastbootDeviceItemClickListener> = mutableListOf()

    class ViewHolder(private val binding: ItemFastbootDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(device: FastbootDevice?, listener: OnFastbootDeviceItemClickListener?) {
            binding.device = device
            binding.onClickListener = listener
            binding.executePendingBindings()
        }
    }

    override fun onFastbootDeviceItemClick(device: FastbootDevice, view: View) {
        listeners.forEach { it.onFastbootDeviceItemClick(device, view) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFastbootDeviceBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), this)
    }

    fun addOnFastbootDeviceItemClickListener(listener: OnFastbootDeviceItemClickListener) {
        listeners.add(listener)
    }

    fun removeOnFastbootDeviceItemClickListener(listener: OnFastbootDeviceItemClickListener) {
        listeners.remove(listener)
    }

    companion object {
        @JvmStatic
        val FASTBOOT_DEVICE_COMPARATOR = object : DiffUtil.ItemCallback<FastbootDevice>() {
            override fun areItemsTheSame(
                oldItem: FastbootDevice,
                newItem: FastbootDevice,
            ): Boolean {
                return oldItem.deviceId == newItem.deviceId
            }

            override fun areContentsTheSame(
                oldItem: FastbootDevice,
                newItem: FastbootDevice,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
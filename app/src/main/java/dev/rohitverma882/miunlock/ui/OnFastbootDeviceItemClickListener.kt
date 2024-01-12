package dev.rohitverma882.miunlock.ui

import android.view.View

import dev.rohitverma882.miunlock.data.FastbootDevice

interface OnFastbootDeviceItemClickListener {
    fun onFastbootDeviceItemClick(device: FastbootDevice, view: View)
}
package dev.rohitverma882.fastboot.sample.ui

import android.view.View

import dev.rohitverma882.fastboot.sample.data.FastbootDevice

interface OnFastbootDeviceItemClickListener {
    fun onFastbootDeviceItemClick(device: FastbootDevice, view: View)
}
package dev.rohitverma882.fastboot

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection

internal interface UsbDeviceManagerListener {
    fun filterDevice(device: UsbDevice): Boolean
    fun onUsbDeviceAttached(device: UsbDevice)
    fun onUsbDeviceDetached(device: UsbDevice)
    fun onUsbDeviceConnected(device: UsbDevice, connection: UsbDeviceConnection)
}
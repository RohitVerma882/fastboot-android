package dev.rohitverma882.fastboot

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import android.util.Log

import androidx.core.content.ContextCompat

import java.lang.ref.WeakReference

internal class UsbDeviceManager(private val context: WeakReference<Context>) {
    private val usbActionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) return
            when (intent.action) {
                UsbManager.ACTION_USB_DEVICE_ATTACHED -> listeners.forEach {
                    val device: UsbDevice? =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            intent.getParcelableExtra(
                                UsbManager.EXTRA_DEVICE, UsbDevice::class.java
                            )
                        } else {
                            @Suppress("DEPRECATION") intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                        }
                    if (device != null && it.filterDevice(device)) it.onUsbDeviceAttached(device)
                }

                UsbManager.ACTION_USB_DEVICE_DETACHED -> listeners.forEach {
                    val device: UsbDevice? =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            intent.getParcelableExtra(
                                UsbManager.EXTRA_DEVICE, UsbDevice::class.java
                            )
                        } else {
                            @Suppress("DEPRECATION") intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                        }
                    if (device != null && it.filterDevice(device)) it.onUsbDeviceDetached(device)
                }
            }
        }
    }

    private val usbPermissionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null || ACTION_USB_PERMISSION != intent.action) return

            synchronized(this) {
                val device: UsbDevice? =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(UsbManager.EXTRA_DEVICE, UsbDevice::class.java)
                    } else {
                        @Suppress("DEPRECATION") intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                    }

                if (device != null && intent.getBooleanExtra(
                        UsbManager.EXTRA_PERMISSION_GRANTED, false
                    )
                ) {
                    connectToDeviceInternal(device)
                } else {
                    Log.d(TAG, "Permission denied for device $device")
                }
            }
        }
    }

    private val listeners = ArrayList<UsbDeviceManagerListener>()
    private var usbManager: UsbManager =
        context.get()?.getSystemService(Context.USB_SERVICE) as UsbManager

    init {
        context.get()?.let {
            val permissionFilter = IntentFilter(ACTION_USB_PERMISSION)
            ContextCompat.registerReceiver(
                it, usbPermissionReceiver, permissionFilter, ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
    }

    fun addUsbDeviceManagerListener(listener: UsbDeviceManagerListener) {
        listeners.add(listener)

        if (listeners.size == 1) {
            val usbActionFilter = IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED)
            usbActionFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
            context.get()?.registerReceiver(usbActionReceiver, usbActionFilter)
        }
    }

    fun removeUsbDeviceManagerListener(listener: UsbDeviceManagerListener) {
        listeners.remove(listener)

        if (listeners.size == 0) {
            context.get()?.unregisterReceiver(usbActionReceiver)
        }
    }

    fun getDevices(): Map<String, UsbDevice> = usbManager.deviceList

    fun connectToDevice(device: UsbDevice) {
        val permissionIntent = PendingIntent.getBroadcast(
            context.get(), 0, Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE
        )

        if (usbManager.hasPermission(device)) {
            connectToDeviceInternal(device)
        } else {
            usbManager.requestPermission(device, permissionIntent)
        }
    }

    private fun connectToDeviceInternal(device: UsbDevice) {
        val connection = usbManager.openDevice(device)

        listeners.forEach {
            if (it.filterDevice(device)) it.onUsbDeviceConnected(device, connection)
        }
    }

    companion object {
        @JvmStatic
        val TAG: String = UsbDeviceManager::class.java.name

        @JvmStatic
        val ACTION_USB_PERMISSION = "miunlock.fastboot.usb.permission"
    }
}
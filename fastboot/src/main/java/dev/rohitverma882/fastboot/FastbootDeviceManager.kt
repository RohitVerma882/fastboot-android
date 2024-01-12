package dev.rohitverma882.fastboot

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbInterface

import dev.rohitverma882.fastboot.transport.UsbTransport

import java.lang.ref.WeakReference

typealias DeviceId = String

object FastbootDeviceManager {
    private const val USB_CLASS = 0xff
    private const val USB_SUBCLASS = 0x42
    private const val USB_PROTOCOL = 0x03

    private val connectedDevices = HashMap<DeviceId, FastbootDeviceContext>()
    private val usbDeviceManager: UsbDeviceManager = UsbDeviceManager(
        WeakReference(FastbootMobile.getApplicationContext())
    )

    private val listeners = ArrayList<FastbootDeviceManagerListener>()

    private val usbDeviceManagerListener = object : UsbDeviceManagerListener {
        override fun filterDevice(device: UsbDevice): Boolean =
            this@FastbootDeviceManager.filterDevice(device)

        @Synchronized
        override fun onUsbDeviceAttached(device: UsbDevice) {
            listeners.forEach {
                it.onFastbootDeviceAttached(device.deviceName)
            }
        }

        @Synchronized
        override fun onUsbDeviceDetached(device: UsbDevice) {
            if (connectedDevices.containsKey(device.deviceName)) {
                connectedDevices[device.deviceName]?.close()
            }
            connectedDevices.remove(device.deviceName)

            listeners.forEach {
                it.onFastbootDeviceDetached(device.deviceName)
            }
        }

        @Synchronized
        override fun onUsbDeviceConnected(
            device: UsbDevice,
            connection: UsbDeviceConnection,
        ) {
            val deviceContext = FastbootDeviceContext(
                UsbTransport(findFastbootInterface(device)!!, connection)
            )

            connectedDevices[device.deviceName]?.close()
            connectedDevices[device.deviceName] = deviceContext

            listeners.forEach {
                it.onFastbootDeviceConnected(device.deviceName, deviceContext)
            }
        }
    }

    @Synchronized
    fun addFastbootDeviceManagerListener(
        listener: FastbootDeviceManagerListener,
    ) {
        listeners.add(listener)

        if (listeners.size == 1) {
            usbDeviceManager.addUsbDeviceManagerListener(usbDeviceManagerListener)
        }
    }

    @Synchronized
    fun removeFastbootDeviceManagerListener(
        listener: FastbootDeviceManagerListener,
    ) {
        listeners.remove(listener)

        if (listeners.size == 0) {
            usbDeviceManager.removeUsbDeviceManagerListener(usbDeviceManagerListener)
        }
    }

    @Synchronized
    fun connectToDevice(deviceId: DeviceId) {
        val device = usbDeviceManager.getDevices().values
            .filter(::filterDevice)
            .firstOrNull { it.deviceName == deviceId }

        if (device != null) usbDeviceManager.connectToDevice(device)
    }

    @Synchronized
    fun getAttachedDeviceIds(): List<DeviceId> {
        return usbDeviceManager.getDevices().values
            .filter(::filterDevice).map { it.deviceName }
            .toList()
    }

    @Synchronized
    fun getConnectedDeviceIds(): List<DeviceId> {
        return connectedDevices.keys.toList()
    }

    @Synchronized
    fun getDeviceContext(
        deviceId: DeviceId,
    ): Pair<DeviceId, FastbootDeviceContext>? {
        return connectedDevices.entries.firstOrNull {
            it.key == deviceId
        }?.toPair()
    }

    private fun filterDevice(device: UsbDevice): Boolean {
        if (device.deviceClass == USB_CLASS && device.deviceSubclass == USB_SUBCLASS && device.deviceProtocol == USB_PROTOCOL) {
            return true
        }
        return findFastbootInterface(device) != null
    }

    private fun findFastbootInterface(device: UsbDevice): UsbInterface? {
        for (i: Int in 0 until device.interfaceCount) {
            val deviceInterface = device.getInterface(i)
            if (deviceInterface.interfaceClass == USB_CLASS && deviceInterface.interfaceSubclass == USB_SUBCLASS && deviceInterface.interfaceProtocol == USB_PROTOCOL) {
                return deviceInterface
            }
        }
        return null
    }
}
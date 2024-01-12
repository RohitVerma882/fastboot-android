package dev.rohitverma882.fastboot

interface FastbootDeviceManagerListener {
    fun onFastbootDeviceAttached(deviceId: DeviceId)
    fun onFastbootDeviceDetached(deviceId: DeviceId)
    fun onFastbootDeviceConnected(
        deviceId: DeviceId,
        deviceContext: FastbootDeviceContext,
    )
}
package dev.rohitverma882.fastboot.sample.ui.devicedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import dev.rohitverma882.fastboot.DeviceId
import dev.rohitverma882.fastboot.FastbootDeviceContext
import dev.rohitverma882.fastboot.FastbootDeviceManager
import dev.rohitverma882.fastboot.FastbootDeviceManagerListener
import dev.rohitverma882.fastboot.sample.data.FastbootDevice

class DeviceDetailsViewModel : ViewModel(), FastbootDeviceManagerListener {
    private var _fastbootDevice = MutableLiveData<FastbootDevice?>()
    val fastbootDevice: LiveData<FastbootDevice?> get() = _fastbootDevice

    private var deviceContext: FastbootDeviceContext? = null

    init {
        FastbootDeviceManager.addFastbootDeviceManagerListener(this)
    }

    fun connectToDevice(deviceId: DeviceId) {
        _fastbootDevice.value = null
        deviceContext?.close()
        FastbootDeviceManager.connectToDevice(deviceId)
    }

    override fun onFastbootDeviceAttached(deviceId: DeviceId) {}

    override fun onFastbootDeviceDetached(deviceId: DeviceId) {
        if (_fastbootDevice.value?.deviceId != deviceId) return
        deviceContext?.close()
        deviceContext = null
    }

    override fun onFastbootDeviceConnected(
        deviceId: DeviceId,
        deviceContext: FastbootDeviceContext,
    ) {
        this.deviceContext = deviceContext
        _fastbootDevice.value = FastbootDevice.fromFastbootDeviceContext(deviceId, deviceContext)
    }

    override fun onCleared() {
        super.onCleared()
        FastbootDeviceManager.removeFastbootDeviceManagerListener(this)
        deviceContext?.close()
        deviceContext = null
    }
}
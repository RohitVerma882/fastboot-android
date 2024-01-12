package dev.rohitverma882.miunlock.data

import dev.rohitverma882.fastboot.DeviceId
import dev.rohitverma882.fastboot.FastbootCommand
import dev.rohitverma882.fastboot.FastbootDeviceContext
import dev.rohitverma882.fastboot.FastbootResponse

data class FastbootDevice(
    val deviceId: DeviceId,
    val serialNumber: String,
    val currentSlot: String,
    val product: String,
    val token: String,
) {
    companion object {
        fun fromDeviceId(deviceId: DeviceId) = FastbootDevice(deviceId, "", "", "", "")

        fun fromFastbootDeviceContext(
            deviceId: DeviceId,
            deviceContext: FastbootDeviceContext,
        ): FastbootDevice =
            FastbootDevice(
                deviceId,
                deviceContext.sendCommand(FastbootCommand.getVar("serialno")).data,
                deviceContext.sendCommand(FastbootCommand.getVar("current-slot")).data,
                deviceContext.sendCommand(FastbootCommand.getVar("product")).data,
                getToken(deviceContext),
            )

        private fun getToken(deviceContext: FastbootDeviceContext): String {
            var response = deviceContext.sendCommand(FastbootCommand.getVar("token"))
            if (response.status == FastbootResponse.ResponseStatus.FAIL || response.status == FastbootResponse.ResponseStatus.UNKNOWN) {
                response = deviceContext.sendCommand(FastbootCommand.oem("get_token"))
            }
            return response.data.replace("\n", "")
        }
    }
}
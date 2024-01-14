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
    val info: String,
) {
    companion object {
        fun fromDeviceId(deviceId: DeviceId) = FastbootDevice(deviceId, "", "", "", "", "")

        fun fromFastbootDeviceContext(
            deviceId: DeviceId,
            deviceContext: FastbootDeviceContext,
        ): FastbootDevice = FastbootDevice(
            deviceId,
            deviceContext.sendCommand(FastbootCommand.getVar("serialno")).data,
            deviceContext.sendCommand(FastbootCommand.getVar("current-slot")).data,
            deviceContext.sendCommand(FastbootCommand.getVar("product")).data,
            getToken(deviceContext),
            deviceContext.sendCommand(FastbootCommand.oem("device-info")).info.joinToString(", ")
        )

        private fun getToken(deviceContext: FastbootDeviceContext): String {
            var response = deviceContext.sendCommand(FastbootCommand.getVar("token"))
            if (response.status == FastbootResponse.ResponseStatus.OKAY) return response.data

            response = deviceContext.sendCommand(FastbootCommand.oem("get_token"))
            val token = StringBuilder()
            response.info.forEach {
                token.append(it.replace("token:", "").trim())
            }
            return token.toString()
        }
    }
}
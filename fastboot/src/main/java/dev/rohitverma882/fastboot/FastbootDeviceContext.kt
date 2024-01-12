package dev.rohitverma882.fastboot

import dev.rohitverma882.fastboot.transport.Transport

class FastbootDeviceContext internal constructor(private val transport: Transport) {
    fun sendCommand(
        command: FastbootCommand, timeout: Int = DEFAULT_TIMEOUT,
        force: Boolean = true,
    ): FastbootResponse {
        if (!transport.isConnected) {
            transport.connect(force)
        }

        val commandStr = command.toString()
        transport.send(commandStr.toByteArray(), timeout)

        val responseBytes = ByteArray(64)
        transport.receive(responseBytes, timeout)
        return FastbootResponse.fromBytes(responseBytes)
    }

    fun close() {
        transport.disconnect()
        transport.close()
    }

    companion object {
        @JvmStatic
        private val DEFAULT_TIMEOUT = 1000
    }
}
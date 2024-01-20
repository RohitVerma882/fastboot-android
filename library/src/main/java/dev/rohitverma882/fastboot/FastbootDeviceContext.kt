/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        return handleResponse(timeout)
    }

    fun sendData(
        buffer: ByteArray, timeout: Int = DEFAULT_TIMEOUT,
        force: Boolean = true,
    ): FastbootResponse {
        if (!transport.isConnected) {
            transport.connect(force)
        }

        transport.send(buffer, timeout)
        return handleResponse(timeout)
    }

    private fun handleResponse(timeout: Int): FastbootResponse {
        val info = ArrayList<String>()
        while (true) {
            val responseBytes = ByteArray(256)
            transport.receive(responseBytes, timeout)
            val response = FastbootResponse.fromBytes(responseBytes)
            if (response.status == FastbootResponse.ResponseStatus.INFO) {
                info.add(response.data)
            } else if (response.status == FastbootResponse.ResponseStatus.TEXT) {
                continue
            } else {
                return FastbootResponse(response.status, response.data, info)
            }
        }
    }

    fun close() {
        transport.disconnect()
        transport.close()
    }

    companion object {
        private const val DEFAULT_TIMEOUT = 1000
    }
}
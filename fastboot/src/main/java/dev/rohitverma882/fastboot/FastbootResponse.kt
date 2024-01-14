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

class FastbootResponse(val status: ResponseStatus, val data: String, val info: List<String>) {
    enum class ResponseStatus(val text: String) {
        INFO("INFO"), FAIL("FAIL"), OKAY("OKAY"), DATA("DATA"), TEXT("TEXT")
    }

    companion object {
        @JvmStatic
        fun fromBytes(arr: ByteArray) = fromString(String(arr))

        @JvmStatic
        fun fromString(str: String): FastbootResponse {
            val status = try {
                ResponseStatus.valueOf(str.substring(0, 4))
            } catch (e: Throwable) {
                ResponseStatus.FAIL
            }

            val data = try {
                str.substring(4)
            } catch (e: Throwable) {
                ""
            }
            return FastbootResponse(status, data, emptyList())
        }
    }
}
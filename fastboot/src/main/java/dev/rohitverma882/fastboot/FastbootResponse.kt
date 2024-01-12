package dev.rohitverma882.fastboot

class FastbootResponse(val status: ResponseStatus, val data: String) {
    enum class ResponseStatus(val text: String) {
        INFO("INFO"), FAIL("FAIL"), OKAY("OKAY"), DATA("DATA"), UNKNOWN("unknown")
    }

    companion object {
        @JvmStatic
        fun fromBytes(arr: ByteArray) = fromString(String(arr))

        @JvmStatic
        fun fromString(str: String): FastbootResponse {
            val status = str.substring(0, 4)
            val data = str.substring(4)

            ResponseStatus.entries.forEach {
                if (it.text == status) {
                    return FastbootResponse(it, data)
                }
            }
            return FastbootResponse(ResponseStatus.UNKNOWN, data)
        }
    }
}
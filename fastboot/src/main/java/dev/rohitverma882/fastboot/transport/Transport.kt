package dev.rohitverma882.fastboot.transport

internal interface Transport {
    var isConnected: Boolean
    fun connect(force: Boolean)
    fun disconnect()
    fun close()
    fun send(buffer: ByteArray, timeout: Int)
    fun receive(buffer: ByteArray, timeout: Int)
}
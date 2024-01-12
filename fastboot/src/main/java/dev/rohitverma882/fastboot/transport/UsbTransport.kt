package dev.rohitverma882.fastboot.transport

import android.hardware.usb.UsbConstants
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbEndpoint
import android.hardware.usb.UsbInterface

internal class UsbTransport(
    private val fastbootInterface: UsbInterface,
    private val connection: UsbDeviceConnection,
) : Transport {
    override var isConnected: Boolean = false

    private lateinit var inEndpoint: UsbEndpoint
    private lateinit var outEndpoint: UsbEndpoint

    init {
        for (i in 0 until fastbootInterface.endpointCount) {
            val e1 = fastbootInterface.getEndpoint(i)
            if (e1.direction == UsbConstants.USB_DIR_IN) {
                inEndpoint = e1
            } else {
                outEndpoint = e1
            }
        }

        if (!this::inEndpoint.isInitialized) throw RuntimeException("No endpoint found for input.")
        if (!this::outEndpoint.isInitialized) throw RuntimeException("No endpoint found for output.")
    }

    override fun connect(force: Boolean) {
        connection.claimInterface(fastbootInterface, force)
        this.isConnected = true
    }

    override fun disconnect() {
        if (!isConnected) return
        connection.releaseInterface(fastbootInterface)
        isConnected = false
    }

    override fun close() {
        disconnect()
        connection.close()
    }

    override fun send(buffer: ByteArray, timeout: Int) {
        connection.bulkTransfer(outEndpoint, buffer, buffer.size, timeout)
    }

    override fun receive(buffer: ByteArray, timeout: Int) {
        connection.bulkTransfer(inEndpoint, buffer, buffer.size, timeout)
    }
}
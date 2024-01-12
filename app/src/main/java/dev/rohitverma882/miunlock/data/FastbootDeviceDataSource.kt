package dev.rohitverma882.miunlock.data

import androidx.paging.DataSource
import androidx.paging.PositionalDataSource

import dev.rohitverma882.fastboot.DeviceId
import dev.rohitverma882.fastboot.FastbootDeviceContext
import dev.rohitverma882.fastboot.FastbootDeviceManager
import dev.rohitverma882.fastboot.FastbootDeviceManagerListener
import dev.rohitverma882.miunlock.data.FastbootDevice.Companion.fromDeviceId

class FastbootDeviceDataSource : PositionalDataSource<FastbootDevice>(),
    FastbootDeviceManagerListener {
    init {
        FastbootDeviceManager.addFastbootDeviceManagerListener(this)
    }

    override fun onFastbootDeviceAttached(deviceId: DeviceId) {
        invalidate()
    }

    override fun onFastbootDeviceDetached(deviceId: DeviceId) {
        invalidate()
    }

    override fun onFastbootDeviceConnected(
        deviceId: DeviceId,
        deviceContext: FastbootDeviceContext,
    ) {
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<FastbootDevice>) {
        val items = FastbootDeviceManager.getAttachedDeviceIds()
            .drop(params.startPosition)
            .take(params.loadSize)
            .map(::fromDeviceId)
        callback.onResult(items)
    }

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<FastbootDevice>,
    ) {
        val allItems = FastbootDeviceManager.getAttachedDeviceIds()
        val items =
            allItems.drop(params.requestedStartPosition)
                .take(params.pageSize)
                .map(::fromDeviceId)

        if (params.placeholdersEnabled) {
            callback.onResult(
                items,
                params.requestedStartPosition,
                allItems.size
            )
        } else {
            callback.onResult(items, params.requestedStartPosition)
        }
    }

    companion object {
        @JvmStatic
        val FACTORY: Factory<Int, FastbootDevice> = object : Factory<Int, FastbootDevice>() {
            private var lastSource: FastbootDeviceDataSource? = null

            override fun create(): DataSource<Int, FastbootDevice> {
                if (lastSource != null) {
                    FastbootDeviceManager.removeFastbootDeviceManagerListener(lastSource!!)
                }
                lastSource = FastbootDeviceDataSource()
                return lastSource!!
            }
        }
    }
}
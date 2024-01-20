package dev.rohitverma882.fastboot.sample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

import dev.rohitverma882.fastboot.sample.data.FastbootDevice
import dev.rohitverma882.fastboot.sample.data.FastbootDeviceDataSource

class MainViewModel : ViewModel() {
    val fastbootDevices: LiveData<PagedList<FastbootDevice>> = LivePagedListBuilder(
        FastbootDeviceDataSource.FACTORY, 10
    ).build()
}
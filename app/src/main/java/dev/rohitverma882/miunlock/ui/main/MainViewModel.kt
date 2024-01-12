package dev.rohitverma882.miunlock.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

import dev.rohitverma882.miunlock.data.FastbootDevice
import dev.rohitverma882.miunlock.data.FastbootDeviceDataSource

class MainViewModel : ViewModel() {
    val fastbootDevices: LiveData<PagedList<FastbootDevice>> = LivePagedListBuilder(
        FastbootDeviceDataSource.FACTORY, 10
    ).build()
}
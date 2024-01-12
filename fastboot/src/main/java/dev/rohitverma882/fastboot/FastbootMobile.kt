package dev.rohitverma882.fastboot

import android.content.Context

import java.lang.ref.WeakReference

internal object FastbootMobile {
    private lateinit var applicationContext: WeakReference<Context>

    @JvmStatic
    @Synchronized
    fun initialize(context: Context) {
        applicationContext = WeakReference(context)
    }

    @JvmStatic
    fun getApplicationContext(): Context {
        if (applicationContext.get() == null) {
            throw RuntimeException("FastbootMobile not initialized.")
        }
        return applicationContext.get()!!
    }
}
package dev.rohitverma882.fastboot

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log

internal class FastbootInitProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        try {
            if (context == null) throw Exception("Failed to get context.")
            FastbootMobile.initialize(context!!)
        } catch (ex: Exception) {
            Log.i(TAG, "Failed to auto initialize the FastbootMobile library", ex)
        }
        return false
    }

    override fun query(
        uri: Uri, strings: Array<String>?, s: String?,
        strings1: Array<String>?, s1: String?,
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri, contentValues: ContentValues?, s: String?,
        strings: Array<String>?,
    ): Int {
        return 0
    }

    companion object {
        @JvmStatic
        private val TAG = FastbootInitProvider::class.java.simpleName
    }
}
package com.moonl.mibandbridge.data

import android.content.Context
import android.os.Build
import java.io.File

interface DataCollector {
    fun collect(): BandData
    fun isRootAvailable(): Boolean
}

class DataCollectorFactory {
    companion object {
        fun create(context: Context): DataCollector {
            return if (isRooted()) {
                RootDataCollector(context)
            } else {
                AccessibilityDataCollector()
            }
        }

        private fun isRooted(): Boolean {
            val paths = arrayOf(
                "/system/app/Superuser.apk",
                "/sbin/su",
                "/system/bin/su",
                "/system/xbin/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/sd/xbin/su",
                "/system/bin/failsafe/su",
                "/data/local/su"
            )
            return paths.any { File(it).exists() }
        }
    }
}
package com.moonl.mibandbridge.data

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.view.accessibility.AccessibilityManager

class AccessibilityDataCollector : DataCollector {
    override fun collect(): BandData {
        val data = BandData()
        // TODO: Implement OCR-based data collection
        // Uses Android Accessibility service to read screen content
        // and extract data shown in Mi Fit app
        data.lastUpdate = System.currentTimeMillis()
        return data
    }

    override fun isRootAvailable(): Boolean = false

    fun isAccessibilityServiceEnabled(context: Context): Boolean {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
        return enabledServices.any {
            it.resolveInfo.serviceInfo.packageName == context.packageName
        }
    }
}
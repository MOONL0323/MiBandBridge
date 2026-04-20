package com.moonl.mibandbridge.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.moonl.mibandbridge.data.BandData
import com.moonl.mibandbridge.data.DataCollectorFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DataCollectionService : Service() {
    private lateinit var collector: com.moonl.mibandbridge.data.DataCollector
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var isRunning = false

    companion object {
        val dataFlow = MutableStateFlow(BandData())
        const val ACTION_START = "com.moonl.mibandbridge.START"
        const val ACTION_STOP = "com.moonl.mibandbridge.STOP"
    }

    override fun onCreate() {
        super.onCreate()
        collector = DataCollectorFactory.create(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startCollection()
            ACTION_STOP -> stopCollection()
        }
        return START_STICKY
    }

    private fun startCollection() {
        if (isRunning) return
        isRunning = true
        scope.launch {
            while (isRunning) {
                val data = collector.collect()
                dataFlow.value = data
                delay(30_000) // Collect every 30 seconds
            }
        }
    }

    private fun stopCollection() {
        isRunning = false
        scope.cancel()
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
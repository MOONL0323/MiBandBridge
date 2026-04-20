package com.moonl.mibandbridge

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.moonl.mibandbridge.data.DataCollectorFactory
import com.moonl.mibandbridge.service.DataCollectionService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var stepsText: TextView
    private lateinit var heartRateText: TextView
    private lateinit var sleepText: TextView
    private lateinit var lastUpdateText: TextView
    private lateinit var statusText: TextView
    private lateinit var btnStart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stepsText = findViewById(R.id.steps)
        heartRateText = findViewById(R.id.heartRate)
        sleepText = findViewById(R.id.sleep)
        lastUpdateText = findViewById(R.id.lastUpdate)
        statusText = findViewById(R.id.status)
        btnStart = findViewById(R.id.btnStart)

        updateStatus()
        setupClickListeners()
        observeData()
    }

    private fun updateStatus() {
        val collector = DataCollectorFactory.create(this)
        val status = if (collector.isRootAvailable()) {
            "ROOT可用 - 使用数据库直读"
        } else {
            "无ROOT - 使用辅助功能(需手动开启)"
        }
        statusText.text = "状态: $status"
    }

    private fun setupClickListeners() {
        btnStart.setOnClickListener {
            val intent = Intent(this, DataCollectionService::class.java).apply {
                action = DataCollectionService.ACTION_START
            }
            startService(intent)
            Toast.makeText(this, "数据收集已启动", Toast.LENGTH_SHORT).show()
            btnStart.text = "数据收集中..."
            btnStart.isEnabled = false
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            DataCollectionService.dataFlow.collect { data ->
                stepsText.text = data.steps.toString()
                heartRateText.text = "${data.heartRate} bpm"
                sleepText.text = data.sleepMinutes.toString()

                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                lastUpdateText.text = "最后更新: ${sdf.format(Date(data.lastUpdate))}"
            }
        }
    }
}
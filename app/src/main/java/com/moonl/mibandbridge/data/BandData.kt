package com.moonl.mibandbridge.data

data class BandData(
    var heartRate: Int = 0,
    var steps: Int = 0,
    var sleepMinutes: Int = 0,
    var lastUpdate: Long = 0
) {
    companion object {
        fun empty() = BandData()
    }
}
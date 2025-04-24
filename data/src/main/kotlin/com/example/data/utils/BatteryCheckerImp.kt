package com.example.data.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.example.domain.util.BatteryChecker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BatteryCheckerImp @Inject constructor(@ApplicationContext private val context: Context):BatteryChecker {
    override fun isSufficient(): Boolean {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, intentFilter)
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct = level * 100 / scale
        return batteryPct >= 20
    }
}
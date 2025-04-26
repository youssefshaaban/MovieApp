package com.example.movieapp

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.data.worker.CacheInvalidationWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class MovieApp:Application(){
    override fun onCreate() {
        super.onCreate()
        scheduleCacheInvalidationWorker()
    }

    private fun scheduleCacheInvalidationWorker() {
        val workRequest = PeriodicWorkRequestBuilder<CacheInvalidationWorker>(
            1, TimeUnit.DAYS
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "invalidate_cache",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
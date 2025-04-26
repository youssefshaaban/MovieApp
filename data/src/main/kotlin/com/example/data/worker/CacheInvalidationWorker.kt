package com.example.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.data.local.MovieDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class CacheInvalidationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val movieDao: MovieDao
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            movieDao.clearAllMovies()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
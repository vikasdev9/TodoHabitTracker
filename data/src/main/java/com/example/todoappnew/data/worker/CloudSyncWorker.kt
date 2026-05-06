package com.example.todoappnew.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoappnew.domain.repository.SyncRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class CloudSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val syncRepository: SyncRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val result = syncRepository.syncCompletedTasks()
        
        if (result.isSuccess) {
            Result.success()
        } else {
            // Retry if it's a network error (Supabase down, etc.)
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}

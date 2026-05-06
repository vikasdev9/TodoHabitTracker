package com.example.todoappnew.data.repository

import com.example.todoappnew.data.local.dao.TaskDao
import com.example.todoappnew.data.remote.model.RemoteTask
import com.example.todoappnew.data.remote.source.SupabaseDataSource
import com.example.todoappnew.data.remote.source.SupabaseStorageSource
import com.example.todoappnew.domain.repository.SyncRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val supabaseDataSource: SupabaseDataSource,
    private val storageSource: SupabaseStorageSource
) : SyncRepository {

    override suspend fun syncCompletedTasks(): Result<Unit> {
        return try {
            val unsyncedCompletedTasks = taskDao.getTasks().first()
                .filter { it.isCompleted && !it.isSynced }
            
            unsyncedCompletedTasks.forEach { entity ->
                var finalImageUrl = entity.imageUri
                var finalVideoUrl = entity.videoUri

                if (entity.imageUri?.startsWith("content://") == true) {
                    val fileName = "img_${entity.id}_${System.currentTimeMillis()}.jpg"
                    finalImageUrl = storageSource.uploadFile(entity.imageUri, fileName)
                }

                if (entity.videoUri?.startsWith("content://") == true) {
                    val fileName = "vid_${entity.id}_${System.currentTimeMillis()}.mp4"
                    finalVideoUrl = storageSource.uploadFile(entity.videoUri, fileName)
                }

                val remoteTask = RemoteTask(
                    id = entity.id, // Now matches UUID format
                    title = entity.title,
                    description = entity.description,
                    isCompleted = entity.isCompleted,
                    scheduledTime = entity.scheduledTime,
                    imageUrl = finalImageUrl,
                    videoUrl = finalVideoUrl
                )
                
                supabaseDataSource.insertTask(remoteTask)
                
                taskDao.updateTask(entity.copy(
                    isSynced = true,
                    imageUri = finalImageUrl,
                    videoUri = finalVideoUrl
                ))
            }
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

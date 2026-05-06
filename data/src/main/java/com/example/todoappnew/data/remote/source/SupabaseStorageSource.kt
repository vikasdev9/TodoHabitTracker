package com.example.todoappnew.data.remote.source

import android.content.Context
import android.net.Uri
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class SupabaseStorageSource @Inject constructor(
    private val supabaseClient: SupabaseClient,
    @ApplicationContext private val context: Context
) {
    companion object {
        const val BUCKET_MEDIA = "task-media"
    }

    suspend fun uploadFile(uriString: String, fileName: String): String? = withContext(Dispatchers.IO) {
        try {
            val uri = Uri.parse(uriString)
            val inputStream = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes() ?: return@withContext null
            inputStream.close()

            val bucket = supabaseClient.storage.from(BUCKET_MEDIA)
            bucket.upload(fileName, bytes)
            
            // Get public URL
            bucket.publicUrl(fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

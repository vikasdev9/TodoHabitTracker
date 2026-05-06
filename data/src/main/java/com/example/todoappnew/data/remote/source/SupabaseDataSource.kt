package com.example.todoappnew.data.remote.source

import com.example.todoappnew.data.remote.model.RemoteTask
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseDataSource @Inject constructor(
    private val supabaseClient: SupabaseClient
) {
    suspend fun insertTask(task: RemoteTask) {
        supabaseClient.postgrest["tasks"].insert(task)
    }

    suspend fun getTasks(): List<RemoteTask> {
        return supabaseClient.postgrest["tasks"].select().decodeList<RemoteTask>()
    }
}

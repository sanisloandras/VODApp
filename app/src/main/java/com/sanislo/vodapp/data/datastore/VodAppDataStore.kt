package com.sanislo.vodapp.data.datastore

import kotlinx.coroutines.flow.Flow

interface VodAppDataStore {
    companion object {
        const val PREFS_NAME = "vod_app_datastore"
    }

    suspend fun saveProgress(id: String, progress: Long)
    suspend fun getProgress(id: String): Long
    fun getWatchedIds(): Flow<List<String>>
}
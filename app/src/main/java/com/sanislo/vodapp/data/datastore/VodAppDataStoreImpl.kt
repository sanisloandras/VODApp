package com.sanislo.vodapp.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VodAppDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : VodAppDataStore {
    override suspend fun saveProgress(id: String, progress: Long) {
        dataStore.edit { preferences ->
            preferences[longPreferencesKey(id)] = progress
        }
    }

    override suspend fun getProgress(id: String): Long {
        return dataStore.data.map {
            it[longPreferencesKey(id)]
        }.firstOrNull() ?: 0L
    }

    override fun getWatchedIds(): Flow<List<String>> {
        return dataStore.data.map { preferences ->
            preferences.asMap().filter {
                it.value is Long
            }.map {
                it.key.name
            }
        }
    }
}
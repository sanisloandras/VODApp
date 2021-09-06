package com.sanislo.vodapp.domain.repo

import com.sanislo.vodapp.data.ApiService
import com.sanislo.vodapp.data.entity.Entry
import com.sanislo.vodapp.data.entity.VodsResponse
import com.sanislo.vodapp.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VodRepository @Inject constructor(
    private val apiService: ApiService,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
) {
    private val cacheMutex = Mutex()
    private var inMemoryVodsResponse: VodsResponse? = null

    suspend fun fetchVods(): VodsResponse {
        return withContext(dispatcher) {
            cacheMutex.withLock {
                val inMemoryVodsResponse = this@VodRepository.inMemoryVodsResponse
                if (inMemoryVodsResponse != null) return@withContext inMemoryVodsResponse
                val vodsResponse = apiService.assets()
                this@VodRepository.inMemoryVodsResponse = vodsResponse
                vodsResponse
            }
        }
    }

    suspend fun vodById(id: String): Entry {
        return fetchVods().entries.first {
            it.id == id
        }
    }

    suspend fun vodByIdSet(ids: Set<String>): List<Entry> {
        return fetchVods().entries.filter {
            ids.contains(it.id)
        }
    }
}
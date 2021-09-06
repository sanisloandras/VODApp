package com.sanislo.vodapp.domain.usecase

import com.sanislo.vodapp.data.datastore.VodAppDataStore
import javax.inject.Inject

class SaveProgressUseCase @Inject constructor(private val vodAppDataStore: VodAppDataStore) {
    suspend operator fun invoke(id: String, progress: Long?) {
        vodAppDataStore.saveProgress(id, progress ?: 0L)
    }
}
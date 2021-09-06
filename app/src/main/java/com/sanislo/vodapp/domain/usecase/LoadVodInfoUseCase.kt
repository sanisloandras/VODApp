package com.sanislo.vodapp.domain.usecase

import com.sanislo.vodapp.data.datastore.VodAppDataStore
import com.sanislo.vodapp.domain.Result
import com.sanislo.vodapp.domain.repo.VodRepository
import javax.inject.Inject

class LoadVodInfoUseCase @Inject constructor(
    private val vodRepository: VodRepository,
    private val vodAppDataStore: VodAppDataStore
) {
    suspend fun invoke(id: String): Result<Pair<String, Long>> {
        return try {
            val url = vodRepository.vodById(id).contents.first().url.replace("http:", "https:")
            val progress = vodAppDataStore.getProgress(id)
            Result.Success(url to progress)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
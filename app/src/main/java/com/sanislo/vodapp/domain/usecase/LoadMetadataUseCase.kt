package com.sanislo.vodapp.domain.usecase

import com.sanislo.vodapp.domain.Result
import com.sanislo.vodapp.domain.model.MetadataItem
import com.sanislo.vodapp.domain.repo.VodRepository
import javax.inject.Inject

class LoadMetadataUseCase @Inject constructor(private val vodRepository: VodRepository) {
    suspend fun invoke(id: String): Result<List<MetadataItem>> {
        return try {
            val metadataItems = vodRepository.vodById(id).metadata.map {
                MetadataItem(it.name, it.value)
            }
            Result.Success(metadataItems)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
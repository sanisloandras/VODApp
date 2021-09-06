package com.sanislo.vodapp.domain.usecase

import com.sanislo.vodapp.domain.Result
import com.sanislo.vodapp.domain.mapper.VodEntryMapper
import com.sanislo.vodapp.domain.model.VodItem
import com.sanislo.vodapp.domain.repo.VodRepository
import javax.inject.Inject

class LoadVodListUseCase @Inject constructor(
    private val vodRepository: VodRepository
) {
    suspend fun invoke(): Result<List<VodItem>> {
        return try {
            val response = vodRepository.fetchVods()
            val vodList = VodEntryMapper().map(response.entries)
            Result.Success(vodList)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
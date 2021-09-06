package com.sanislo.vodapp.domain.usecase

import com.sanislo.vodapp.data.datastore.VodAppDataStore
import com.sanislo.vodapp.domain.Result
import com.sanislo.vodapp.domain.mapper.VodEntryMapper
import com.sanislo.vodapp.domain.model.VodItem
import com.sanislo.vodapp.domain.repo.VodRepository
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class LoadWatchedVodListUseCase @Inject constructor(
    private val vodRepository: VodRepository,
    private val vodAppDataStore: VodAppDataStore,
) {
    suspend fun invoke(): Result<List<VodItem>> {
        return try {
            val watchedIdSet = vodAppDataStore.getWatchedIds().take(1).single().toSet()
            val watchedVodEntries = vodRepository.vodByIdSet(watchedIdSet)
            Result.Success(VodEntryMapper().map(watchedVodEntries))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
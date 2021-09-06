package com.sanislo.vodapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.sanislo.vodapp.data.datastore.VodAppDataStore
import com.sanislo.vodapp.domain.Result
import com.sanislo.vodapp.domain.model.VodItem
import com.sanislo.vodapp.domain.repo.VodRepository
import com.sanislo.vodapp.domain.usecase.LoadWatchedVodListUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoadWatchedVodListUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var vodRepository: VodRepository

    @Mock
    private lateinit var vodAppDataStore: VodAppDataStore

    private lateinit var loadWatchedVodListUseCase: LoadWatchedVodListUseCase

    @Before
    fun setupViewModel() {
        loadWatchedVodListUseCase = LoadWatchedVodListUseCase(vodRepository, vodAppDataStore)
    }

    @Test
    fun test() = coroutineRule.runBlockingTest {
        Mockito.`when`(vodAppDataStore.getWatchedIds()).thenReturn(flowOf(listOf("id_0", "id_2")))
        Mockito.`when`(vodRepository.vodByIdSet(any()))
            .thenReturn(listOf(TestData.ENTRY_0, TestData.ENTRY_2))
        val actual = loadWatchedVodListUseCase.invoke()
        assertEquals(
            Result.Success(
                listOf(
                    VodItem("id_0", "title_0", "url_0"),
                    VodItem("id_2", "title_2", "url_2")
                )
            ),
            actual
        )
        Mockito.verify(vodRepository, times(1)).vodByIdSet(setOf("id_0", "id_2"))
        Mockito.verify(vodAppDataStore, times(1)).getWatchedIds()
    }
}
package com.sanislo.vodapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.times
import com.sanislo.vodapp.data.ApiService
import com.sanislo.vodapp.domain.repo.VodRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class VodRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    private val testDispatcher = coroutineRule.testDispatcher

    @Mock
    private lateinit var apiService: ApiService

    private lateinit var vodRepository: VodRepository

    @Before
    fun setupViewModel() {
        vodRepository = VodRepository(apiService, testDispatcher)
    }

    @Test
    fun test_fetchVods() = coroutineRule.runBlockingTest {
        Mockito.`when`(apiService.assets()).thenReturn(TestData.vodsResponse)
        val actual = vodRepository.fetchVods()
        assertEquals(TestData.vodsResponse, actual)
        vodRepository.fetchVods()
        vodRepository.fetchVods()
        Mockito.verify(apiService, times(1)).assets()
    }

    @Test
    fun test_vodById() = coroutineRule.runBlockingTest {
        Mockito.`when`(apiService.assets()).thenReturn(TestData.vodsResponse)
        val actual = vodRepository.vodById("id_1")
        assertEquals(TestData.ENTRY_1, actual)
    }

    @Test
    fun test_vodByIdSet() = coroutineRule.runBlockingTest {
        Mockito.`when`(apiService.assets()).thenReturn(TestData.vodsResponse)
        val actual = vodRepository.vodByIdSet(setOf("id_0", "id_2"))
        assertEquals(listOf(TestData.ENTRY_0, TestData.ENTRY_2), actual)
    }
}
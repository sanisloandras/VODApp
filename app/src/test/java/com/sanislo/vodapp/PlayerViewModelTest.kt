package com.sanislo.vodapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.sanislo.vodapp.data.datastore.VodAppDataStore
import com.sanislo.vodapp.domain.Result
import com.sanislo.vodapp.domain.usecase.LoadVodInfoUseCase
import com.sanislo.vodapp.presentation.player.PlayerViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PlayerViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var vodAppDataStore: VodAppDataStore

    @Mock
    private lateinit var loadVodInfoUseCase: LoadVodInfoUseCase

    private lateinit var viewModel: PlayerViewModel

    @Before
    fun setupViewModel() {
        viewModel =
            PlayerViewModel(vodAppDataStore, loadVodInfoUseCase, mainCoroutineRule.CoroutineScope())
    }

    @Test
    fun test_Result_Success() = mainCoroutineRule.runBlockingTest {
        Mockito.`when`(loadVodInfoUseCase.invoke(any()))
            .thenReturn(Result.Success("mock_url" to 0L))
        viewModel.load("mock_id")
        viewModel.playbackInfo.test {
            assertEquals("mock_url" to 0L, awaitItem())
        }
    }

    @Test
    fun test_onStop() = mainCoroutineRule.runBlockingTest {
        viewModel.onStop("mock_id", 42L)
        Mockito.verify(vodAppDataStore, times(1)).saveProgress("mock_id", 42L)
    }

    @Test
    fun test_onEnded() = mainCoroutineRule.runBlockingTest {
        viewModel.onEnded("mock_id")
        Mockito.verify(vodAppDataStore, times(1)).saveProgress("mock_id", 0L)
    }
}
package com.sanislo.vodapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.times
import com.sanislo.vodapp.domain.Result
import com.sanislo.vodapp.domain.model.VodItem
import com.sanislo.vodapp.domain.usecase.LoadVodListUseCase
import com.sanislo.vodapp.presentation.vods.VodListViewModel
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
class VodListViewModelTest {
    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var vodListUseCase: LoadVodListUseCase

    private lateinit var viewModel: VodListViewModel

    @Before
    fun setupViewModel() {
        viewModel = VodListViewModel(vodListUseCase)
    }

    @Test
    fun test_Result_Success() = mainCoroutineRule.runBlockingTest {
        val data = listOf(
            VodItem("0", "title_0", "image_0"),
            VodItem("1", "title_1", "image_1")
        )
        val result = Result.Success(data)
        Mockito.`when`(vodListUseCase.invoke()).thenReturn(result)
        viewModel.vodList.test {
            assertEquals(emptyList<VodItem>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.isLoading.test {
            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.fetch()
        Mockito.verify(vodListUseCase, times(1)).invoke()
        viewModel.vodList.test {
            assertEquals(data, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.isLoading.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun test_Result_Error() = mainCoroutineRule.runBlockingTest {
        val result = Result.Error(Exception("mock_exception"))
        Mockito.`when`(vodListUseCase.invoke())
            .thenReturn(result)

        viewModel.isLoading.test {
            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.fetch()

        viewModel.isLoading.test {
            assertEquals(false, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
        viewModel.vodList.test {
            assertEquals(emptyList<VodItem>(), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
        viewModel.action.test {
            val action = awaitItem()
            assertEquals("mock_exception", (action as? VodListViewModel.Action.Error)?.message)
            cancelAndConsumeRemainingEvents()
        }
        Mockito.verify(vodListUseCase, times(1)).invoke()
    }

    @Test
    fun test_onClick() = mainCoroutineRule.runBlockingTest {
        viewModel.onClick(VodItem("mock_id", "title", "url"))
        viewModel.action.test {
            val action = awaitItem()
            assertEquals("mock_id", (action as VodListViewModel.Action.NavigateToPlayer).id)
            cancelAndConsumeRemainingEvents()
        }
    }
}
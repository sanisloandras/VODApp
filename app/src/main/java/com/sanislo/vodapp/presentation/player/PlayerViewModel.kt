package com.sanislo.vodapp.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanislo.vodapp.di.ApplicationScope
import com.sanislo.vodapp.domain.Result
import com.sanislo.vodapp.domain.usecase.LoadVodInfoUseCase
import com.sanislo.vodapp.domain.usecase.SaveProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val saveProgressUseCase: SaveProgressUseCase,
    private val loadVodInfoUseCase: LoadVodInfoUseCase,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {
    private var shouldSaveProgressOnStop = true

    private val _playbackInfo = MutableStateFlow<Pair<String, Long>?>(null)
    val playbackInfo = _playbackInfo.asStateFlow()

    fun load(id: String) {
        viewModelScope.launch {
            when (val result = loadVodInfoUseCase.invoke(id)) {
                is Result.Success -> _playbackInfo.value = result.data
            }
        }
    }

    fun onStop(id: String, progress: Long?) {
        if (!shouldSaveProgressOnStop) return
        applicationScope.launch {
            saveProgressUseCase(id, progress)
        }
    }

    /**
     * When we reach the end of the video, we save the progress with value 0.
     * This is to solve a bug when the user opens a vod which he already watched until the end.
     * This way we will still have the vod in the watch history, and the vod will re-start.
     * Should consult with a ux designer for a better experience :)
     */
    fun onEnded(id: String) {
        shouldSaveProgressOnStop = false
        applicationScope.launch {
            saveProgressUseCase(id, PROGRESS_START)
        }
    }

    companion object {
        private const val PROGRESS_START = 0L
    }
}
package com.sanislo.vodapp.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanislo.vodapp.domain.Result
import com.sanislo.vodapp.domain.model.VodItem
import com.sanislo.vodapp.domain.usecase.LoadWatchedVodListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val loadWatchedVodListUseCase: LoadWatchedVodListUseCase
) : ViewModel() {
    private val _vodList = MutableStateFlow<List<VodItem>>(emptyList())
    val vodList = _vodList.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isEmpty = MutableStateFlow(false)
    val isEmpty = _isEmpty.asStateFlow()

    private val _action = Channel<Action>()
    val action = _action.receiveAsFlow()

    init {
        fetch()
    }

    private fun fetch() {
        viewModelScope.launch {
            when (val result = loadWatchedVodListUseCase.invoke()) {
                is Result.Error -> {
                    _isLoading.value = false
                    _action.send(Action.Error(result.exception.message))
                }
                is Result.Success -> {
                    _isLoading.value = false
                    _isEmpty.value = result.data.isEmpty()
                    _vodList.value = result.data
                }
            }
        }
    }

    fun onClick(vodItem: VodItem) {
        viewModelScope.launch {
            _action.send(Action.NavigateToPlayer(vodItem.id))
        }
    }

    sealed class Action {
        class NavigateToPlayer(val id: String) : Action()
        class Error(val message: String?) : Action()
    }
}
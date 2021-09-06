package com.sanislo.vodapp.presentation.vods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanislo.vodapp.domain.Result
import com.sanislo.vodapp.domain.model.VodItem
import com.sanislo.vodapp.domain.usecase.LoadVodListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VodListViewModel @Inject constructor(
    private val vodListUseCase: LoadVodListUseCase
) : ViewModel() {
    private val _vodList = MutableStateFlow<List<VodItem>>(emptyList())
    val vodList = _vodList.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _action = Channel<Action>()
    val action = _action.receiveAsFlow()

    fun fetch() {
        viewModelScope.launch {
            when (val result = vodListUseCase.invoke()) {
                is Result.Error -> {
                    _isLoading.value = false
                    _action.send(Action.Error(result.exception.message))
                }
                is Result.Success -> {
                    _isLoading.value = false
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
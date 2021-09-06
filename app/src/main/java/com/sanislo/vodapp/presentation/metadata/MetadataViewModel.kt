package com.sanislo.vodapp.presentation.metadata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanislo.vodapp.domain.Result
import com.sanislo.vodapp.domain.model.MetadataItem
import com.sanislo.vodapp.domain.usecase.LoadMetadataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MetadataViewModel @Inject constructor(private val loadMetadataUseCase: LoadMetadataUseCase) :
    ViewModel() {
    private val _metadataItems = MutableStateFlow<List<MetadataItem>>(emptyList())
    val metadataItems = _metadataItems.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _action = Channel<Action>()
    val action = _action.receiveAsFlow()

    fun loadMetadata(id: String) {
        viewModelScope.launch {
            when (val result = loadMetadataUseCase.invoke(id)) {
                is Result.Error -> _action.send(Action.Error(result.exception.message))
                is Result.Success -> {
                    _metadataItems.value = result.data
                    _isLoading.value = false
                }
            }
        }
    }

    sealed class Action {
        class Error(val message: String?) : Action()
    }
}
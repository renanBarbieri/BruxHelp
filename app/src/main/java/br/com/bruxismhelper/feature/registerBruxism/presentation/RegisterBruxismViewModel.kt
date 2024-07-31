package br.com.bruxismhelper.feature.registerBruxism.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bruxismhelper.feature.registerBruxism.domain.repository.RegisterBruxismRepository
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.RegisterBruxismViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterBruxismViewModel @Inject constructor(
    val repository: RegisterBruxismRepository,
    val mapper: RegsiterBruxismViewMapper
) : ViewModel() {
    private val _viewState = MutableStateFlow(RegisterBruxismViewState())
    val viewState: StateFlow<RegisterBruxismViewState> = _viewState

    fun updateIsEating(isEating: Boolean) {
        _viewState.update { it.copy(isEating = isEating) }
    }

    fun updateSelectedActivity(selectedActivity: String) {
        _viewState.update { it.copy(selectedActivity = selectedActivity) }
    }

    fun updateIsInPain(isInPain: Boolean) {
        _viewState.update { it.copy(isInPain = isInPain) }
    }

    fun updatePainLevel(painLevel: Int) {
        _viewState.update { it.copy(painLevel = painLevel) }
    }

    fun updateStressLevel(stressLevel: Int) {
        _viewState.update { it.copy(stressLevel = stressLevel) }
    }

    fun updateAnxietyLevel(anxietyLevel: Int) {
        _viewState.update { it.copy(anxietyLevel = anxietyLevel) }
    }

    fun updateSelectableImageCheck(index: Int) {
        _viewState.update { state ->
            val updatedImages = state.selectableImages.toMutableList().apply {
                this[index] = this[index].copy(isSelected = !this[index].isSelected)
            }
            state.copy(selectableImages = updatedImages)
        }
    }

    fun submitForm() {
        viewModelScope.launch {
            repository.submitForm(mapper.fromViewToDomain(_viewState.value))
        }
    }
}


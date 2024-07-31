package br.com.bruxismhelper.feature.registerBruxism.presentation

import androidx.lifecycle.ViewModel
import br.com.bruxismhelper.R
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.BottomLeftIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.BottomRightIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.RegisterBruxismViewState
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.SelectableImage
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.TopLeftIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.TopRightIdentifier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegisterBruxismViewModel @Inject constructor() : ViewModel() {
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
}
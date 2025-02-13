package br.com.bruxismhelper.feature.registerBruxism.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bruxismhelper.feature.registerBruxism.domain.repository.RegisterBruxismRepository
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.RegisterBruxismViewState
import br.com.bruxismhelper.feature.registerBruxism.repository.UserNotFound
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterBruxismViewModel @Inject constructor(
    private val repository: RegisterBruxismRepository,
    private val mapper: RegisterBruxismViewMapper
) : ViewModel() {
    private val _viewState = MutableStateFlow(RegisterBruxismViewState())
    val viewState: StateFlow<RegisterBruxismViewState> = _viewState

    fun updateIsEating(isEating: Boolean) {
        _viewState.updateAndVerifyMandatory {
            it.copy(registerBruxismForm = it.registerBruxismForm.copy(isEating = isEating))
        }
    }

    fun updateSelectedActivity(selectedActivity: String) {
        _viewState.updateAndVerifyMandatory {
            it.copy(
                registerBruxismForm = it.registerBruxismForm.copy(
                    selectedActivity = selectedActivity
                )
            )
        }
    }

    fun updateIsInPain(isInPain: Boolean) {
        _viewState.updateAndVerifyMandatory {
            it.copy(registerBruxismForm = it.registerBruxismForm.copy(isInPain = isInPain))
        }
    }

    fun updatePainLevel(painLevel: Int) {
        _viewState.updateAndVerifyMandatory {
            it.copy(registerBruxismForm = it.registerBruxismForm.copy(painLevel = painLevel))
        }
    }

    fun updateStressLevel(stressLevel: Int) {
        _viewState.updateAndVerifyMandatory {
            it.copy(registerBruxismForm = it.registerBruxismForm.copy(stressLevel = stressLevel))
        }
    }

    fun updateAnxietyLevel(anxietyLevel: Int) {
        _viewState.updateAndVerifyMandatory {
            it.copy(registerBruxismForm = it.registerBruxismForm.copy(anxietyLevel = anxietyLevel))
        }
    }

    fun updateSelectableImageCheck(index: Int) {
        _viewState.updateAndVerifyMandatory { state ->
            val updatedImages = state.registerBruxismForm.selectableImages.toMutableList().apply {
                this[index] = this[index].copy(isSelected = !this[index].isSelected)
            }
            state.copy(registerBruxismForm = state.registerBruxismForm.copy(selectableImages = updatedImages))
        }
    }

    fun submitForm() {
        _viewState.update {
            it.copy(
                showLoading = true,
                formSubmitResult = null
            )
        }
        viewModelScope.launch {
            val formSubmitResult =
                repository.submitForm(mapper.fromViewToDomain(_viewState.value)).fold(
                    onSuccess = { Result.success(it) },
                    onFailure = {
                        if (it is UserNotFound) Result.success(Unit)
                        else Result.failure(it)
                    }
                )
            _viewState.update { state ->
                state.copy(
                    showLoading = false,
                    formSubmitResult = formSubmitResult
                )
            }
        }
    }

    fun onCloseAlertRequest() {
        _viewState.update { it.copy(formSubmitResult = null) }
    }

    private fun MutableStateFlow<RegisterBruxismViewState>.updateAndVerifyMandatory(function: (RegisterBruxismViewState) -> RegisterBruxismViewState) {
        update {
            function(it)
        }

        with(value.registerBruxismForm) {
            val allMandatoryFilled = when {
                isEating -> true
                selectedActivity.isEmpty() -> false
                isInPain -> selectableImages.any { it.isSelected }
                else -> true
            }

            update {
                it.copy(
                    allMandatoryFieldsFilled = allMandatoryFilled
                )
            }
        }
    }
}


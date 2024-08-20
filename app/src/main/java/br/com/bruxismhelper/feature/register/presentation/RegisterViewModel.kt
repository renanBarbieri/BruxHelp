package br.com.bruxismhelper.feature.register.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bruxismhelper.feature.register.domain.model.Dentist
import br.com.bruxismhelper.feature.register.domain.repository.RegisterRepository
import br.com.bruxismhelper.feature.register.presentation.model.FrequencyViewObject
import br.com.bruxismhelper.feature.register.presentation.model.OralHabitViewObject
import br.com.bruxismhelper.feature.register.presentation.model.RegisterFields
import br.com.bruxismhelper.feature.register.presentation.model.RegisterViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: RegisterRepository,
    private val mapper: RegisterViewMapper
) : ViewModel() {

    private val _viewState =
        MutableStateFlow(RegisterViewState(RegisterFields(repository.getAvailableDentists())))
    val viewState: StateFlow<RegisterViewState> = _viewState

    //region update fields
    fun updateFullName(name: String) {
        _viewState.update {
            it.copy(
                registerForm = it.registerForm.copy(fullName = name)
            )
        }
    }

    fun updateEmail(email: String) {
        _viewState.update {
            it.copy(
                registerForm = it.registerForm.copy(email = email)
            )
        }
    }

    fun updateDentist(dentist: String) {
        _viewState.update {
            it.copy(
                registerForm = it.registerForm.copy(dentist = Dentist(dentist))
            )
        }
    }

    fun updateContinuousMedicines(medicines: String) {
        _viewState.update {
            it.copy(
                registerForm = it.registerForm.copy(continuousMedicines = medicines)
            )
        }
    }

    fun updateCaffeineConsumptionQuantity(quantity: String) {
        _viewState.update {
            it.copy(
                registerForm = it.registerForm.copy(
                    caffeineConsumption = it.registerForm.caffeineConsumption.copy(
                        quantity = quantity.toIntOrNull()
                    )
                )
            )
        }
    }

    fun updateCaffeineConsumptionFrequency(frequency: String) {
        _viewState.update {
            it.copy(
                registerForm = it.registerForm.copy(
                    caffeineConsumption = it.registerForm.caffeineConsumption.copy(
                        frequency = FrequencyViewObject.fromString(frequency)
                    )
                )
            )
        }
    }

    fun updateSmokingQuantity(quantity: String) {
        _viewState.update {
            it.copy(
                registerForm = it.registerForm.copy(
                    smoking = it.registerForm.smoking.copy(quantity = quantity.toIntOrNull())
                )
            )
        }
    }

    fun updateSmokingFrequency(frequency: String) {
        _viewState.update {
            it.copy(
                registerForm = it.registerForm.copy(
                    smoking = it.registerForm.smoking.copy(frequency = FrequencyViewObject.fromString(frequency))
                )
            )
        }
    }

    fun updateOralHabits(habit: OralHabitViewObject, checked: Boolean) {
        _viewState.update {
            val currentHabits = it.registerForm.oralHabits.toMutableList()

            if (checked) {
                currentHabits.add(habit)
            } else {
                currentHabits.remove(habit)
            }

            it.copy(
                registerForm = it.registerForm.copy(
                    oralHabits = currentHabits
                )
            )
        }
    }
    //endregion

    fun submitForm() {
        _viewState.update { it.copy(error = null) }

        viewModelScope.launch {
            repository.submitForm(mapper.fromViewToDomain(_viewState.value.registerForm))
                .onSuccess {
                    _viewState.update {
                        it.copy(
                            submitSuccess = true,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    _viewState.update {
                        it.copy(
                            submitSuccess = false,
                            error = error
                        )
                    }
                }
        }
    }

    fun onCloseAlertRequest() {
        _viewState.update { it.copy(error = null) }
    }
}
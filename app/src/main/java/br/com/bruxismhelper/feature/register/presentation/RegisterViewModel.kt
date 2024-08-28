package br.com.bruxismhelper.feature.register.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bruxismhelper.feature.alarm.AlarmSchedulerFacade
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
    private val mapper: RegisterViewMapper,
    private val alertFacade: AlarmSchedulerFacade
) : ViewModel() {

    private val _viewState =
        MutableStateFlow(RegisterViewState(RegisterFields(repository.getAvailableDentists())))
    val viewState: StateFlow<RegisterViewState> = _viewState

    //region update fields
    fun updateFullName(name: String) {
        _viewState.updateAndVerifyMandatory {
            it.copy(
                registerForm = it.registerForm.copy(fullName = name)
            )
        }
    }

    fun updateEmail(email: String) {
        _viewState.updateAndVerifyMandatory {
            it.copy(
                registerForm = it.registerForm.copy(email = email)
            )
        }
    }

    fun updateDentist(dentist: String) {
        _viewState.updateAndVerifyMandatory {
            it.copy(
                registerForm = it.registerForm.copy(dentist = Dentist(dentist))
            )
        }
    }

    fun updateContinuousMedicines(medicines: String) {
        _viewState.updateAndVerifyMandatory {
            it.copy(
                registerForm = it.registerForm.copy(continuousMedicines = medicines)
            )
        }
    }

    fun updateCaffeineConsumptionQuantity(quantity: String) {
        _viewState.updateAndVerifyMandatory {
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
        _viewState.updateAndVerifyMandatory {
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
        _viewState.updateAndVerifyMandatory {
            it.copy(
                registerForm = it.registerForm.copy(
                    smoking = it.registerForm.smoking.copy(quantity = quantity.toIntOrNull())
                )
            )
        }
    }

    fun updateSmokingFrequency(frequency: String) {
        _viewState.updateAndVerifyMandatory {
            it.copy(
                registerForm = it.registerForm.copy(
                    smoking = it.registerForm.smoking.copy(
                        frequency = FrequencyViewObject.fromString(
                            frequency
                        )
                    )
                )
            )
        }
    }

    fun updateOralHabits(habit: OralHabitViewObject, checked: Boolean) {
        _viewState.updateAndVerifyMandatory {
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

    private fun scheduleAlarm(){
        alertFacade.scheduleNextAlarm(null)
    }

    fun ignoreForm() {
        scheduleAlarm()
    }

    fun submitForm() {
        _viewState.update { it.copy(error = null) }

        if(_viewState.value.allMandatoryFieldsFilled.not()) {
            _viewState.update { it.copy(error = Throwable()) }
        }

        viewModelScope.launch {
            _viewState.update { it.copy(showLoading = true) }
            repository.submitForm(mapper.fromViewToDomain(_viewState.value.registerForm))
                .onSuccess {
                    scheduleAlarm()

                    _viewState.update {
                        it.copy(
                            submitSuccess = true,
                            error = null,
                            showLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _viewState.update {
                        it.copy(
                            submitSuccess = false,
                            error = error,
                            showLoading = false
                        )
                    }
                }
        }
    }

    fun onCloseAlertRequest() {
        _viewState.update { it.copy(error = null) }
    }

    private fun MutableStateFlow<RegisterViewState>.updateAndVerifyMandatory(function: (RegisterViewState) -> RegisterViewState) {
        update {
            function(it)
        }

        with(value.registerForm) {
            val allMandatoryFilled =
                fullName.isNotBlank() && email.isNotBlank() && dentist.name.isNotBlank()
            update {
                it.copy(
                    allMandatoryFieldsFilled = allMandatoryFilled
                )
            }
        }
    }
}
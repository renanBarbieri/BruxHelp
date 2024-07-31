package br.com.bruxismhelper.feature.register.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bruxismhelper.feature.register.domain.model.Dentist
import br.com.bruxismhelper.feature.register.domain.repository.RegisterRepository
import br.com.bruxismhelper.feature.register.presentation.model.FrequencyViewObject
import br.com.bruxismhelper.feature.register.presentation.model.OralHabitViewObject
import br.com.bruxismhelper.feature.register.presentation.model.RegisterFields
import br.com.bruxismhelper.feature.register.presentation.model.RegisterFormField
import br.com.bruxismhelper.feature.register.presentation.model.RegisterViewState
import br.com.bruxismhelper.feature.registerBruxism.presentation.RegisterBruxismViewState
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

    fun updateField(field: RegisterFormField, value: Any) {
        when (field) {
            RegisterFormField.FULL_NAME -> _viewState.update {
                it.copy(
                    registerForm = it.registerForm.copy(fullName = value as String)
                )
            }

            RegisterFormField.EMAIL -> _viewState.update {
                it.copy(
                    registerForm = it.registerForm.copy(
                        email = value as String
                    )
                )
            }

            RegisterFormField.DENTIST -> _viewState.update {
                it.copy(
                    registerForm = it.registerForm.copy(
                        dentist = Dentist(value as String)
                    )
                )
            }

            RegisterFormField.CONTINUOUS_MEDICATIONS -> _viewState.update {
                it.copy(
                    registerForm = it.registerForm.copy(
                        continuousMedications = value as String
                    )
                )
            }

            RegisterFormField.CAFFEINE_CONSUMPTION_QUANTITY -> _viewState.update {
                it.copy(
                    registerForm = it.registerForm.copy(
                        caffeineConsumption = it.registerForm.caffeineConsumption.copy(
                            quantity = value.asStringToIntOrNull()
                        )
                    )
                )
            }

            RegisterFormField.CAFFEINE_CONSUMPTION_FREQUENCY -> _viewState.update {
                it.copy(
                    registerForm = it.registerForm.copy(
                        caffeineConsumption = it.registerForm.caffeineConsumption.copy(
                            frequency = FrequencyViewObject.fromString(
                                value as String
                            )
                        )
                    )
                )
            }

            RegisterFormField.SMOKING_QUANTITY -> _viewState.update {
                it.copy(
                    registerForm = it.registerForm.copy(
                        smoking = it.registerForm.smoking.copy(quantity = value.asStringToIntOrNull())
                    )
                )
            }

            RegisterFormField.SMOKING_FREQUENCY -> _viewState.update {
                it.copy(
                    registerForm = it.registerForm.copy(
                        smoking = it.registerForm.smoking.copy(
                            frequency = FrequencyViewObject.fromString(
                                value as String
                            )
                        )
                    )
                )
            }

            RegisterFormField.ORAL_HABITS -> {
                _viewState.update {
                    @Suppress("UNCHECKED_CAST") val fieldValue =
                        value as Pair<OralHabitViewObject, Boolean>
                    val habit = fieldValue.first
                    val checked = fieldValue.second

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
        }
    }

    private fun Any.asStringToIntOrNull(): Int? {
        val thisAsString = this as? String

        return if (thisAsString.isNullOrBlank().not()) {
            thisAsString!!.toInt()
        } else {
            null
        }
    }

    fun submitForm() {
        viewModelScope.launch {
            repository.submitForm(mapper.fromViewToDomain(_viewState.value.registerForm))
        }
    }
}
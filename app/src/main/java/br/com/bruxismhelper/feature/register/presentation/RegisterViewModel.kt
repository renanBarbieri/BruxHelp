package br.com.bruxismhelper.feature.register.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.com.bruxismhelper.feature.register.domain.model.Dentist
import br.com.bruxismhelper.feature.register.domain.repository.RegisterRepository
import br.com.bruxismhelper.feature.register.presentation.model.FrequencyViewObject
import br.com.bruxismhelper.feature.register.presentation.model.OralHabitViewObject
import br.com.bruxismhelper.feature.register.presentation.model.RegisterFields
import br.com.bruxismhelper.feature.register.presentation.model.RegisterFormField
import br.com.bruxismhelper.feature.register.presentation.model.RegisterViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: RegisterRepository,
    private val mapper: RegisterViewMapper
) :
    ViewModel() {

    var viewState by mutableStateOf(RegisterViewState(RegisterFields(repository.getAvailableDentists())))
        private set

    fun updateField(field: RegisterFormField, value: Any) {
        when (field) {
            RegisterFormField.FULL_NAME -> viewState = viewState.copy(
                registerForm = viewState.registerForm.copy(
                    fullName = value as String
                )
            )

            RegisterFormField.EMAIL -> viewState = viewState.copy(
                registerForm = viewState.registerForm.copy(
                    email = value as String
                )
            )

            RegisterFormField.DENTIST -> viewState = viewState.copy(
                registerForm = viewState.registerForm.copy(
                    dentist = Dentist(value as String)
                )
            )

            RegisterFormField.CONTINUOUS_MEDICATIONS -> viewState = viewState.copy(
                registerForm = viewState.registerForm.copy(
                    continuousMedications = value as String
                )
            )

            RegisterFormField.CAFFEINE_CONSUMPTION_QUANTITY -> viewState = viewState.copy(
                registerForm = viewState.registerForm.copy(
                    caffeineConsumption = viewState.registerForm.caffeineConsumption.copy(quantity = value as Int)
                )
            )

            RegisterFormField.CAFFEINE_CONSUMPTION_FREQUENCY -> viewState = viewState.copy(
                registerForm = viewState.registerForm.copy(
                    caffeineConsumption = viewState.registerForm.caffeineConsumption.copy(
                        frequency = FrequencyViewObject.fromString(
                            value as String
                        )
                    )
                )
            )

            RegisterFormField.SMOKING_QUANTITY -> viewState = viewState.copy(
                registerForm = viewState.registerForm.copy(
                    smoking = viewState.registerForm.smoking.copy(quantity = value as Int)
                )
            )

            RegisterFormField.SMOKING_FREQUENCY -> viewState = viewState.copy(
                registerForm = viewState.registerForm.copy(
                    smoking = viewState.registerForm.smoking.copy(
                        frequency = FrequencyViewObject.fromString(
                            value as String
                        )
                    )
                )
            )

            RegisterFormField.ORAL_HABITS -> {
                @Suppress("UNCHECKED_CAST") val fieldValue =
                    value as Pair<OralHabitViewObject, Boolean>
                val habit = fieldValue.first
                val checked = fieldValue.second

                val currentHabits = viewState.registerForm.oralHabits.toMutableList()

                if (checked) {
                    currentHabits.add(habit)
                } else {
                    currentHabits.remove(habit)
                }

                viewState = viewState.copy(
                    registerForm = viewState.registerForm.copy(
                        oralHabits = currentHabits
                    )
                )
            }
        }
    }

    fun submitForm() {
        repository.submitForm(mapper.fromViewToDomain(viewState.registerForm))
    }
}
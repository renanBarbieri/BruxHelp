package br.com.bruxismhelper.feature.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(val repository: RegisterRepository) : ViewModel() {

    var viewState by mutableStateOf(RegisterViewState(RegisterFields(repository.getAvailableDentists())))
        private set

    fun updateField(field: RegisterFormField, value: Any){
        when(field) {
            RegisterFormField.FULL_NAME -> viewState = viewState.copy(
                formFilled = viewState.formFilled.copy(
                    fullName = value as String
                )
            )
            RegisterFormField.EMAIL -> viewState = viewState.copy(
                formFilled = viewState.formFilled.copy(
                    email = value as String
                )
            )
            RegisterFormField.DENTIST -> viewState = viewState.copy(
                formFilled = viewState.formFilled.copy(
                    dentist = Dentist(value as String)
                )
            )
            RegisterFormField.CONTINUOUS_MEDICATIONS -> viewState = viewState.copy(
                formFilled = viewState.formFilled.copy(
                    continuousMedications = value as String
                )
            )
            RegisterFormField.CAFFEINE_CONSUMPTION_QUANTITY -> viewState = viewState.copy(
                formFilled = viewState.formFilled.copy(
                    caffeineConsumption = viewState.formFilled.caffeineConsumption.copy(quantity = value as Int)
                )
            )
            RegisterFormField.CAFFEINE_CONSUMPTION_FREQUENCY -> viewState = viewState.copy(
                formFilled = viewState.formFilled.copy(
                    caffeineConsumption = viewState.formFilled.caffeineConsumption.copy(frequency = Frequency.fromString(value as String))
                )
            )
            RegisterFormField.SMOKING_QUANTITY -> viewState = viewState.copy(
                formFilled = viewState.formFilled.copy(
                    smoking = viewState.formFilled.smoking.copy(quantity = value as Int)
                )
            )
            RegisterFormField.SMOKING_FREQUENCY -> viewState = viewState.copy(
                formFilled = viewState.formFilled.copy(
                    smoking = viewState.formFilled.smoking.copy(frequency = Frequency.fromString(value as String))
                )
            )
            RegisterFormField.ORAL_HABITS -> viewState = viewState.copy(
                formFilled = viewState.formFilled.copy(
                    oralHabits = listOf() //TODO add/remove items
                )
            )
        }
    }

    fun submitForm() {
        repository.submitForm(viewState.formFilled)
    }
}

enum class RegisterFormField {
    FULL_NAME,
    EMAIL,
    DENTIST,
    CONTINUOUS_MEDICATIONS,
    CAFFEINE_CONSUMPTION_QUANTITY,
    CAFFEINE_CONSUMPTION_FREQUENCY,
    SMOKING_QUANTITY,
    SMOKING_FREQUENCY,
    ORAL_HABITS,
}

interface RegisterRepository {
    fun getAvailableDentists(): List<Dentist>

    fun submitForm(formFilled: FormFilled)
}

data class RegisterViewState(
    val formFields: RegisterFields,
    val formFilled: FormFilled = FormFilled()
)

data class RegisterFields(
    val dentists: List<Dentist>
)

data class FormFilled(
    val fullName: String = "",
    val email: String = "",
    val dentist: Dentist = Dentist(),
    val continuousMedications: String = "",
    val caffeineConsumption: CaffeineConsumption = CaffeineConsumption(),
    val smoking: Smoking = Smoking(),
    val oralHabits: List<OralHabit>? = null
)

data class Dentist(
    val name: String = ""
)

data class CaffeineConsumption(
    val quantity: Int? = null,   // e.g., "3 cups"
    val frequency: Frequency? = null  // e.g., "daily"
)

enum class Frequency {
    DAILY,
    WEEKLY,
    BY_WEEKLY;

    companion object {
        fun fromString(value: String): Frequency {
            return DAILY
        }
    }
}

data class Smoking(
    val quantity: Int? = null,   // e.g., "10 cigarettes"
    val frequency: Frequency? = null   // e.g., "daily"
)

enum class OralHabit {
    NAIL_BITING,
    OBJECT_BITING,
    LIP_BITING
}

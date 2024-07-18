package br.com.bruxismhelper.feature.register.presentation

import br.com.bruxismhelper.feature.register.domain.model.CaffeineConsumption
import br.com.bruxismhelper.feature.register.domain.model.Frequency
import br.com.bruxismhelper.feature.register.domain.model.OralHabit
import br.com.bruxismhelper.feature.register.domain.model.RegisterForm
import br.com.bruxismhelper.feature.register.domain.model.SmokingConsumption
import br.com.bruxismhelper.feature.register.presentation.model.ConsumptionViewObject
import br.com.bruxismhelper.feature.register.presentation.model.FrequencyViewObject
import br.com.bruxismhelper.feature.register.presentation.model.OralHabitViewObject
import br.com.bruxismhelper.feature.register.presentation.model.RegisterFormViewObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterViewMapper @Inject constructor() {

    fun fromViewToDomain(formView: RegisterFormViewObject): RegisterForm {
        return RegisterForm(
            fullName = formView.fullName,
            email = formView.email,
            dentist = formView.dentist,
            continuousMedications = formView.continuousMedications,
            caffeineConsumption = formView.caffeineConsumption.toCaffeineConsumption(),
            smoking = formView.smoking.toSmokingConsumption(),
            oralHabits = formView.oralHabits.toDomain(),
        )
    }

    private fun ConsumptionViewObject.toCaffeineConsumption(): CaffeineConsumption {
        return CaffeineConsumption(
            this.quantity,
            this.frequency?.toDomain()
        )
    }

    private fun ConsumptionViewObject.toSmokingConsumption(): SmokingConsumption {
        return SmokingConsumption(
            this.quantity,
            this.frequency?.toDomain()
        )
    }

    private fun FrequencyViewObject.toDomain(): Frequency {
        return when(this) {
            FrequencyViewObject.DAILY -> Frequency.DAILY
            FrequencyViewObject.WEEKLY -> Frequency.WEEKLY
            else -> Frequency.BY_WEEKLY
        }
    }

    private fun List<OralHabitViewObject>.toDomain(): List<OralHabit> {
        return this.map {
            when(it) {
                OralHabitViewObject.NAIL_BITING -> OralHabit.NAIL_BITING
                OralHabitViewObject.OBJECT_BITING -> OralHabit.OBJECT_BITING
                OralHabitViewObject.LIP_BITING -> OralHabit.LIP_BITING
            }
        }
    }
}
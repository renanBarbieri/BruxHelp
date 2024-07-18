package br.com.bruxismhelper.feature.register.presentation.model

import br.com.bruxismhelper.feature.register.domain.model.Dentist

data class RegisterFormViewObject(
    val fullName: String = "",
    val email: String = "",
    val dentist: Dentist = Dentist(),
    val continuousMedications: String = "",
    val caffeineConsumption: ConsumptionViewObject = ConsumptionViewObject(),
    val smoking: ConsumptionViewObject = ConsumptionViewObject(),
    val oralHabits: List<OralHabitViewObject> = listOf()
)
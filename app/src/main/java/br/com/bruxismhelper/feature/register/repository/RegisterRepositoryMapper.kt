package br.com.bruxismhelper.feature.register.repository

import br.com.bruxismhelper.feature.register.domain.model.Consumption
import br.com.bruxismhelper.feature.register.domain.model.Dentist
import br.com.bruxismhelper.feature.register.domain.model.RegisterForm
import javax.inject.Inject

class RegisterRepositoryMapper @Inject constructor(){

    fun mapFromDomain(registerForm: RegisterForm): Map<String, Any> {
        return registerForm.toMap()
    }

    private fun RegisterForm.toMap(): Map<String, Any> {
        return mapOf(
            "fullName" to fullName,
            "email" to email,
            "dentist" to dentist.toMap(),
            "continuousMedicines" to continuousMedicines,
            "caffeineConsumption" to caffeineConsumption.toMap(),
            "smoking" to smoking.toMap(),
            "oralHabits" to oralHabits.map { it.name }
        )
    }

    private fun Dentist.toMap(): Map<String, Any> {
        return mapOf(
            "name" to name
        )
    }

    private fun Consumption.toMap(): Map<String, Any> {
        return mapOf(
            "quantity" to (quantity ?: 0),
            "frequency" to (frequency?.name ?: "")
        )
    }
}
package br.com.bruxismhelper.feature.register.domain.model

data class RegisterForm(
    val fullName: String = "",
    val email: String = "",
    val dentist: Dentist = Dentist(),
    val continuousMedicines: String = "",
    val caffeineConsumption: Consumption = CaffeineConsumption(),
    val smoking: Consumption = SmokingConsumption(),
    val oralHabits: List<OralHabit> = listOf()
)
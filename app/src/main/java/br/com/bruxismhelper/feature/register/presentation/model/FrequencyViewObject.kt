package br.com.bruxismhelper.feature.register.presentation.model

import br.com.bruxismhelper.R

enum class FrequencyViewObject(val fieldNameRes: Int) {
    DAILY(R.string.register_option_frequency_daily),
    WEEKLY(R.string.register_option_frequency_weekly),
    BY_WEEKLY(R.string.register_option_frequency_by_week);

    companion object {
        fun fromString(value: String): FrequencyViewObject {
            return when(value) {
                "Diária" -> DAILY
                "Semanal" -> WEEKLY
                else -> BY_WEEKLY
            }
        }
    }
}
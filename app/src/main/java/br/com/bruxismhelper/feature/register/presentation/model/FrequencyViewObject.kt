package br.com.bruxismhelper.feature.register.presentation.model

import br.com.bruxismhelper.R

enum class FrequencyViewObject(val fieldNameRes: Int) {
    NO_USAGE(R.string.register_option_no_usage),
    DAILY(R.string.register_option_frequency_daily),
    WEEKLY(R.string.register_option_frequency_weekly),
    BY_WEEKLY(R.string.register_option_frequency_by_week);

    companion object {
        fun fromString(value: String): FrequencyViewObject {
            return when(value) {
                "Diária" -> DAILY
                "Semanal" -> WEEKLY
                "Quinzenal" -> BY_WEEKLY
                else -> NO_USAGE
            }
        }
    }
}
package br.com.bruxismhelper.feature.register.domain.model

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
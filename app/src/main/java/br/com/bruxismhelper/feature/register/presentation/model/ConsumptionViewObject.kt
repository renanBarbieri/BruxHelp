package br.com.bruxismhelper.feature.register.presentation.model

import br.com.bruxismhelper.emptyString

data class ConsumptionViewObject(
    val quantity: Int? = null,
    val frequency: FrequencyViewObject? = null
) {
    fun frequencyToStringOrEmpty(): String {
        return frequency?.toString() ?: emptyString()
    }
}
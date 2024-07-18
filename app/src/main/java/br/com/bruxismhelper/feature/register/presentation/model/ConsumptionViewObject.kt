package br.com.bruxismhelper.feature.register.presentation.model

import br.com.bruxismhelper.emptyResString

data class ConsumptionViewObject(
    val quantity: Int? = null,
    val frequency: FrequencyViewObject? = null
) {
    fun frequencyToResStringOrEmpty(): Int {
        return frequency?.fieldNameRes ?: emptyResString()
    }
}
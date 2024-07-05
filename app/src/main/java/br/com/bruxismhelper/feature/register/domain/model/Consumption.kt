package br.com.bruxismhelper.feature.register.domain.model

open class Consumption(
    val quantity: Int? = null,
    val frequency: Frequency? = null
) {
    open fun copy(
        quantity: Int? = this.quantity,
        frequency: Frequency? = this.frequency
    ): Consumption {
        return Consumption(quantity, frequency)
    }
}
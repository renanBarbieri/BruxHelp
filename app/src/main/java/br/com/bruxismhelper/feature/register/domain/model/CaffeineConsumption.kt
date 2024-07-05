package br.com.bruxismhelper.feature.register.domain.model

class CaffeineConsumption(
    quantity: Int? = null,
    frequency: Frequency? = null
) : Consumption(quantity, frequency)
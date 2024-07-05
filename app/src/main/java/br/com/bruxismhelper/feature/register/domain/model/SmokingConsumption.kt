package br.com.bruxismhelper.feature.register.domain.model

class SmokingConsumption(
    quantity: Int? = null,
    frequency: Frequency? = null
) : Consumption(quantity, frequency) //TODO MAPEAMENTO PARA VIEW
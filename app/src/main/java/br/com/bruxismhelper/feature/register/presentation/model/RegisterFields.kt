package br.com.bruxismhelper.feature.register.presentation.model

import br.com.bruxismhelper.feature.register.domain.model.Dentist

data class RegisterFields(
    val dentists: List<Dentist>
)
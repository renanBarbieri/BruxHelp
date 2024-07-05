package br.com.bruxismhelper.feature.register.domain.repository

import br.com.bruxismhelper.feature.register.domain.model.Dentist
import br.com.bruxismhelper.feature.register.domain.model.RegisterForm

interface RegisterRepository {
    fun getAvailableDentists(): List<Dentist>

    fun submitForm(registerForm: RegisterForm)
}
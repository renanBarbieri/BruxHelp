package br.com.bruxismhelper.feature.register.repository

import br.com.bruxismhelper.feature.register.domain.model.Dentist
import br.com.bruxismhelper.feature.register.domain.model.RegisterForm
import br.com.bruxismhelper.feature.register.domain.repository.RegisterRepository
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(): RegisterRepository {

    override fun getAvailableDentists(): List<Dentist> {
        TODO("Not yet implemented")
    }

    override fun submitForm(registerForm: RegisterForm) {
        TODO("Not yet implemented")
    }
}
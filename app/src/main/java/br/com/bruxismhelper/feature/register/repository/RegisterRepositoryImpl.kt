package br.com.bruxismhelper.feature.register.repository

import br.com.bruxismhelper.feature.register.domain.model.Dentist
import br.com.bruxismhelper.feature.register.domain.model.RegisterForm
import br.com.bruxismhelper.feature.register.domain.repository.RegisterRepository
import br.com.bruxismhelper.feature.register.repository.source.RegisterRemoteDataSource
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val remoteDataSource: RegisterRemoteDataSource,
    private val mapper: RegisterRepositoryMapper
): RegisterRepository {

    override fun getAvailableDentists(): List<Dentist> {
        return listOf(Dentist(name = "Dra. Nathália Celestino"), Dentist(name = "Outro") )
    }

    override fun submitForm(registerForm: RegisterForm) {
        remoteDataSource.submitForm(mapper.mapFromDomain(registerForm))
    }
}
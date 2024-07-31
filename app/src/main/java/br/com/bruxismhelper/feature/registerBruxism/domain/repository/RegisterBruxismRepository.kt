package br.com.bruxismhelper.feature.registerBruxism.domain.repository

import br.com.bruxismhelper.feature.registerBruxism.domain.model.RegisterBruxismForm

interface RegisterBruxismRepository {
    suspend fun submitForm(registerForm: RegisterBruxismForm)
}
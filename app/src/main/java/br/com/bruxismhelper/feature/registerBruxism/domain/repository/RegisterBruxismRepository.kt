package br.com.bruxismhelper.feature.registerBruxism.domain.repository

import br.com.bruxismhelper.feature.registerBruxism.domain.model.RegisterBruxismForm
import br.com.bruxismhelper.feature.registerBruxism.domain.model.ResponseBruxismForm

interface RegisterBruxismRepository {
    suspend fun submitForm(registerForm: RegisterBruxismForm): Result<Unit>

    suspend fun getResponses(): Result<List<ResponseBruxismForm>>
}
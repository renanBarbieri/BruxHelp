package br.com.bruxismhelper.feature.register.presentation.model

import br.com.bruxismhelper.feature.register.domain.model.RegisterForm

data class RegisterViewState(
    val formFields: RegisterFields,
    val registerForm: RegisterForm = RegisterForm()
)
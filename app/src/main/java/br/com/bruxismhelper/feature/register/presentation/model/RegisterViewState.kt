package br.com.bruxismhelper.feature.register.presentation.model

data class RegisterViewState(
    val formFields: RegisterFields,
    val registerForm: RegisterFormViewObject = RegisterFormViewObject()
)
package br.com.bruxismhelper.feature.registerBruxism.presentation.model

data class RegisterBruxismViewState(
    val formSubmitResult: Result<Unit>? = null,
    val showLoading: Boolean = false,
    val registerBruxismForm: RegisterBruxismFormViewObject = RegisterBruxismFormViewObject(),
    val allMandatoryFieldsFilled: Boolean = false,
)
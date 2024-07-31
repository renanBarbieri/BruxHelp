package br.com.bruxismhelper.feature.registerBruxism.domain.model

data class RegisterBruxismForm(
    val isEating: Boolean = false,
    val selectedActivity: String? = "",
    val isInPain: Boolean? = false,
    val painLevel: Int? = 0,
    val stressLevel: Int? = 0,
    val anxietyLevel: Int? = 0,
    val selectableImages: List<BruxismRegion>?
)
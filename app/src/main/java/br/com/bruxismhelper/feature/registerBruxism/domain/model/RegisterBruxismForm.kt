package br.com.bruxismhelper.feature.registerBruxism.domain.model

data class RegisterBruxismForm(
    val isEating: Boolean,
    val selectedActivity: String?,
    val stressLevel: Int?,
    val anxietyLevel: Int?,
    val isInPain: Boolean?,
    val painLevel: Int?,
    val selectableImages: List<BruxismRegion>?
)
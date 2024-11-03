package br.com.bruxismhelper.feature.registerBruxism.domain.model

import java.util.Calendar

open class RegisterBruxismForm(
    val isEating: Boolean,
    val selectedActivity: String?,
    val stressLevel: Int?,
    val anxietyLevel: Int?,
    val isInPain: Boolean?,
    val painLevel: Int?,
    val selectableImages: List<BruxismRegion>?
)

class ResponseBruxismForm(
    val createdAt: Calendar,
    isEating: Boolean,
    selectedActivity: String?,
    stressLevel: Int?,
    anxietyLevel: Int?,
    isInPain: Boolean?,
    painLevel: Int?,
    selectableImages: List<BruxismRegion>?,
) : RegisterBruxismForm(
    isEating,
    selectedActivity,
    stressLevel,
    anxietyLevel,
    isInPain,
    painLevel,
    selectableImages,
)
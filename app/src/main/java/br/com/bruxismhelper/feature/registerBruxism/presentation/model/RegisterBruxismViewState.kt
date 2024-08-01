package br.com.bruxismhelper.feature.registerBruxism.presentation.model

import br.com.bruxismhelper.R

data class RegisterBruxismViewState(
    val isEating: Boolean = false,
    val selectedActivity: String = "",
    val isInPain: Boolean = false,
    val painLevel: Int = 0,
    val stressLevel: Int = 0,
    val anxietyLevel: Int = 0,
    val selectableImages: List<SelectableImage> = mockSelectableImage
)

private val mockSelectableImage = listOf(
    SelectableImage(
        id = AtmLeftIdentifier,
        isSelected = false,
        imageRes = R.drawable.pain_test
    ),
    SelectableImage(
        id = AtmRightIdentifier,
        isSelected = false,
        imageRes = R.drawable.pain_test
    ),
    SelectableImage(
        id = MasseterLeftIdentifier,
        isSelected = false,
        imageRes = R.drawable.pain_test
    ),
    SelectableImage(
        id = MasseterRightIdentifier,
        isSelected = false,
        imageRes = R.drawable.pain_test
    ),
    SelectableImage(
        id = TemporalLeftIdentifier,
        isSelected = false,
        imageRes = R.drawable.pain_test
    ),
    SelectableImage(
        id = TemporalRightIdentifier,
        isSelected = false,
        imageRes = R.drawable.pain_test
    ),
)
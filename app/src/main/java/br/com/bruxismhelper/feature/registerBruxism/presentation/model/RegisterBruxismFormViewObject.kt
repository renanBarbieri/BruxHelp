package br.com.bruxismhelper.feature.registerBruxism.presentation.model

import br.com.bruxismhelper.R

data class RegisterBruxismFormViewObject(
    val isEating: Boolean = false,
    val selectedActivity: String = "",
    val isInPain: Boolean = false,
    val painLevel: Int = 0,
    val stressLevel: Int = 0,
    val anxietyLevel: Int = 0,
    val selectableImages: List<SelectableImage> = mockSelectableImage,
)


val mockSelectableImage = listOf(
    SelectableImage(
        id = AtmLeftIdentifier,
        isSelected = false,
        imageRes = R.drawable.left_top
    ),
    SelectableImage(
        id = AtmRightIdentifier,
        isSelected = false,
        imageRes = R.drawable.right_top
    ),
    SelectableImage(
        id = MasseterLeftIdentifier,
        isSelected = false,
        imageRes = R.drawable.left_middle
    ),
    SelectableImage(
        id = MasseterRightIdentifier,
        isSelected = false,
        imageRes = R.drawable.right_middle
    ),
    SelectableImage(
        id = TemporalLeftIdentifier,
        isSelected = false,
        imageRes = R.drawable.left_bottom
    ),
    SelectableImage(
        id = TemporalRightIdentifier,
        isSelected = false,
        imageRes = R.drawable.right_bottom
    ),
)
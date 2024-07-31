package br.com.bruxismhelper.feature.registerBruxism.presentation.model

import androidx.annotation.DrawableRes

data class SelectableImage(
    val id: Identifier,
    val isSelected: Boolean,
    @DrawableRes val imageRes: Int
)
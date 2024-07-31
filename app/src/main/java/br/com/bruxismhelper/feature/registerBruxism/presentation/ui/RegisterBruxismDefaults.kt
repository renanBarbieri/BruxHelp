package br.com.bruxismhelper.feature.registerBruxism.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object RegisterBruxismDefaults {
    val fieldsOutsidePadding = 24.dp

    fun Modifier.cardPadding() = this.padding(
        top = 8.dp, bottom = 8.dp,
        start = 8.dp, end = 8.dp
    )
}
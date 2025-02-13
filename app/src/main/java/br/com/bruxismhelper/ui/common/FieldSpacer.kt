package br.com.bruxismhelper.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FieldSpacer() {
    Spacer(modifier = Modifier.height(16.dp))
}

fun LazyListScope.spacerField() {
    item {
        FieldSpacer()
    }
}
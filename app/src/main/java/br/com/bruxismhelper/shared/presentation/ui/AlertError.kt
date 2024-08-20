package br.com.bruxismhelper.shared.presentation.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import br.com.bruxismhelper.BuildConfig

@Composable
internal fun Throwable.AlertError(
    title: String,
    productionText: String,
    confirmButtonText: String = "",
    onDismissRequest: (() -> Unit)? = null,
    onConfirmRequest: (() -> Unit)? = null,
) {
    AlertDialog(
        title = {
            Text(
                textAlign = TextAlign.Center,
                text = title
            )
        },
        text = {
            val textMessage = if (BuildConfig.DEBUG) this.message.orEmpty() else productionText
            Text(text = textMessage)
        },
        onDismissRequest = {
            onDismissRequest?.invoke()
        },
        confirmButton = {
            onConfirmRequest?.let {
                Button(onClick = { it.invoke() }) {
                    Text(text = confirmButtonText)
                }
            }
        }
    )
}
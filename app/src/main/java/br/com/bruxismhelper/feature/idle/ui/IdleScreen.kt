package br.com.bruxismhelper.feature.idle.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.bruxismhelper.BuildConfig
import br.com.bruxismhelper.R
import br.com.bruxismhelper.feature.idle.ui.waiting.WaitingDefaults.waitingTextFontSize
import br.com.bruxismhelper.feature.idle.ui.waiting.WaitingIcon
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme

@Composable
fun IdleScreen(
    centerIcon: @Composable () -> Unit,
    messageStringRes: Int,
    backgroundColor: Color? = null,
) {
    Surface(
        color = backgroundColor.alphaOrSurface(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            centerIcon()
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(id = messageStringRes),
                fontSize = waitingTextFontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text("Versão do aplicativo: ${BuildConfig.VERSION_NAME}")
        }
    }
}

@Composable
private fun Color?.alphaOrSurface(): Color {
    return this?.copy(alpha = .9f) ?: MaterialTheme.colorScheme.surface
}

@Preview(showBackground = true)
@Composable
fun WaitingScreenPreview() {
    BruxismHelperTheme {
        IdleScreen(
            centerIcon = { WaitingIcon() },
            messageStringRes = R.string.waiting_message
        )
    }
}
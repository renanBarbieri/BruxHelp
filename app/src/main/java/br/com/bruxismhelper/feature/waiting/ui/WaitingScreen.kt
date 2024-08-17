package br.com.bruxismhelper.feature.waiting.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.bruxismhelper.R
import br.com.bruxismhelper.feature.waiting.ui.WaitingDefaults.waitingTextFontSize
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme

@Composable
fun WaitingScreen(
    centerIcon: @Composable () -> Unit = { WaitingIcon() },
    messageStringRes: Int,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        centerIcon()
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = messageStringRes),
            fontSize = waitingTextFontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WaitingScreenPreview() {
    BruxismHelperTheme {
        WaitingScreen(messageStringRes = R.string.waiting_message)
    }
}
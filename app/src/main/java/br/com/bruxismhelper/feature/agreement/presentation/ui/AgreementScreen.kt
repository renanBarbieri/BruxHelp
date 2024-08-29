package br.com.bruxismhelper.feature.agreement.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.bruxismhelper.R
import br.com.bruxismhelper.feature.agreement.presentation.AgreementViewModel
import br.com.bruxismhelper.feature.idle.ui.waiting.WaitingDefaults.waitingTextFontSize
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme

@Composable
fun AgreementScreen(
    viewModel: AgreementViewModel = hiltViewModel(),
    agreementText: String = stringResource(id = R.string.agreement_term),
    onAgreed: () -> Unit = {}
) {
    Surface {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text(
                    text = agreementText,
                    fontSize = waitingTextFontSize,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Justify
                )
            }

            item {
                Row(
                    modifier = Modifier.padding(bottom = 24.dp,)
                ) {
                    Button(onClick = {
                        viewModel.onAgree()
                        onAgreed()
                    }) {
                        Text(text = stringResource(id = R.string.agreement_button))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WaitingScreenPreview() {
    BruxismHelperTheme {
        AgreementScreen()
    }
}
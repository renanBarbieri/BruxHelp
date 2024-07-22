package br.com.bruxismhelper.feature.registerBruxism

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.bruxismhelper.R
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme

@Composable
fun StressLevelSlider() {
    //TODO observar a mudança do grade
    var stressLevel by remember { mutableStateOf(0f) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(id = R.string.register_bruxism_label_stress_level),
            modifier = Modifier.fillMaxWidth()
        )

        Slider(
            value = stressLevel,
            onValueChange = { stressLevel = it },
            valueRange = 0f..10f,
            steps = 9,
            modifier = Modifier.padding(3.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painterResource(id = R.drawable.smile_friendly),
                contentDescription = null,
                modifier = Modifier.size(25.dp),
            )
            Image(
                painterResource(id = R.drawable.smile_stress),
                contentDescription = null,
                modifier = Modifier.size(25.dp),
            )
        }
        Text(
            text = when (stressLevel.toInt()) {
                in 0..3 -> stringResource(id = R.string.register_bruxism_label_stress_level_low)
                in 4..7 -> stringResource(id = R.string.register_bruxism_label_stress_level_medium)
                else -> stringResource(id = R.string.register_bruxism_label_stress_level_high)
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun StressPreview() {
    BruxismHelperTheme {
        StressLevelSlider()
    }
}
package br.com.bruxismhelper.feature.registerBruxism

import android.content.res.Resources.Theme
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.bruxismhelper.R
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme

@Composable
fun LevelSlider(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    resultLevelText: String,
    @DrawableRes lowLevelIconRes: Int? = null,
    @DrawableRes highLevelIconRes: Int? = null,
    levelValue: Int = 0,
    onLevelChange: (value: Int) -> Unit = {},
    levelRange: ClosedFloatingPointRange<Float> = 0f..10f,
    steps: Int = 10
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            text = stringResource(titleRes),
        )

        Slider(
            value = levelValue.toFloat(),
            onValueChange = { onLevelChange(it.toInt()) },
            valueRange = levelRange,
            steps = steps,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.tertiary,
                activeTrackColor = MaterialTheme.colorScheme.tertiary,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainer
            ),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            lowLevelIconRes?.let {
                Image(
                    painterResource(it),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp),
                )
            }

            highLevelIconRes?.let {
                Image(
                    painterResource(it),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp),
                )
            }
        }
        Text(
            text = resultLevelText,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun StressPreview() {
    BruxismHelperTheme {
        LevelSlider(
            titleRes = R.string.register_bruxism_label_stress_level,
            resultLevelText = "Exemplo"
        )
    }
}
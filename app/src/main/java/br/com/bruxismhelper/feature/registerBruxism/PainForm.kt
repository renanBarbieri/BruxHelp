package br.com.bruxismhelper.feature.registerBruxism

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.bruxismhelper.R
import br.com.bruxismhelper.ui.common.FieldSpacer
import br.com.bruxismhelper.ui.common.FieldSwitch

@Composable
fun PainForm(
    isInPain: MutableState<Boolean>,
    painLevel: MutableState<Int>,
    stressLevel: MutableState<Int>,
    anxietyLevel: MutableState<Int>,
) {
    FieldSwitch(
        name = R.string.register_bruxism_pain_switch,
        checked = isInPain.value,
        onCheckedChange = { isInPain.value = it }
    )

    FieldSpacer()

    AnimatedVisibility(visible = isInPain.value) {
        Column {
            Card(elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )) {
                LevelSlider(
                    modifier = Modifier.cardPadding(),
                    titleRes = R.string.register_bruxism_label_pain_level,
                    resultLevelText = stringResource(
                        id = R.string.register_bruxism_label_pain_level_result,
                        painLevel.value
                    ),
                    levelValue = painLevel.value,
                    onLevelChange = {
                        painLevel.value = it
                    }
                )
            }

            FieldSpacer()

            Card(elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )) {
                //TODO Trocar pelas imagens reais
                ImageGridWithCheckboxes(
                    modifier = Modifier.cardPadding(),
                    images = listOf(
                        R.drawable.pain_test,
                        R.drawable.pain_test,
                        R.drawable.pain_test,
                        R.drawable.pain_test,
                    )
                )
            }

            FieldSpacer()

            Card(elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )) {
                LevelSlider(
                    modifier = Modifier.cardPadding(),
                    titleRes = R.string.register_bruxism_label_stress_level,
                    resultLevelText = when (stressLevel.value) {
                        in 0..3 -> stringResource(id = R.string.register_bruxism_label_stress_level_low)
                        in 4..7 -> stringResource(id = R.string.register_bruxism_label_stress_level_medium)
                        else -> stringResource(id = R.string.register_bruxism_label_stress_level_high)
                    },
                    lowLevelIconRes = R.drawable.smile_friendly,
                    highLevelIconRes = R.drawable.smile_stress,
                    levelValue = stressLevel.value,
                    onLevelChange = {
                        stressLevel.value = it
                    }
                )
            }

            FieldSpacer()

            Card(elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )) {
                LevelSlider(
                    modifier = Modifier.cardPadding(),
                    titleRes = R.string.register_bruxism_label_anxiety_level,
                    resultLevelText = when (anxietyLevel.value) {
                        in 0..3 -> stringResource(id = R.string.register_bruxism_label_anxiety_level_low)
                        in 4..7 -> stringResource(id = R.string.register_bruxism_label_anxiety_level_medium)
                        else -> stringResource(id = R.string.register_bruxism_label_anxiety_level_high)
                    },
                    lowLevelIconRes = R.drawable.smile_friendly,
                    highLevelIconRes = R.drawable.smile_stress,
                    levelValue = anxietyLevel.value,
                    onLevelChange = {
                        anxietyLevel.value = it
                    }
                )
            }
        }
    }
}

private fun Modifier.cardPadding() = this.padding(
    top = 8.dp, bottom = 8.dp,
    start = 8.dp, end = 8.dp
)
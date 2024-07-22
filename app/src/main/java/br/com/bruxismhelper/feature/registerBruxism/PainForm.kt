package br.com.bruxismhelper.feature.registerBruxism

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.bruxismhelper.R
import br.com.bruxismhelper.ui.common.FieldSpacer
import br.com.bruxismhelper.ui.common.FieldSwitch

@Composable
fun PainForm(
    isInPain: MutableState<Boolean>,
    painLevel: MutableState<Int>,
) {
    FieldSwitch(
        name = R.string.register_bruxism_pain_switch,
        checked = isInPain.value,
        onCheckedChange = { isInPain.value = it }
    )

    if (isInPain.value) {
        FieldSpacer()

        //TODO externalizar o slider de dor
        Text(stringResource(id = R.string.register_bruxism_label_pain_level))
        Slider(
            value = painLevel.value.toFloat(),
            onValueChange = { painLevel.value = it.toInt() },
            valueRange = 0f..10f,
            steps = 10,
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = stringResource(
            id = R.string.register_bruxism_label_pain_level_result,
            painLevel.value
        ))

        FieldSpacer()

        //TODO Trocar pelas imagens reais
        ImageGridWithCheckboxes(
            images = listOf(
                R.drawable.pain_test,
                R.drawable.pain_test,
                R.drawable.pain_test,
                R.drawable.pain_test,
            )
        )

        FieldSpacer()

        StressLevelSlider()
    }
}
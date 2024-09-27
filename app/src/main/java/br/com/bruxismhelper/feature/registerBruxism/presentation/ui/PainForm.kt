package br.com.bruxismhelper.feature.registerBruxism.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.bruxismhelper.R
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.SelectableImage
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.mockSelectableImage
import br.com.bruxismhelper.feature.registerBruxism.presentation.ui.RegisterBruxismDefaults.cardPadding
import br.com.bruxismhelper.ui.common.FieldSpacer
import br.com.bruxismhelper.ui.common.FieldSwitch
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme

@Composable
fun PainForm(
    isInPain: Boolean = true,
    onPainChanged: (isInPain: Boolean) -> Unit = {},
    painLevel: Int = 0,
    onPainLevelChanged: (level: Int) -> Unit = {},
    selectableImages: List<SelectableImage> = listOf(),
    onImageSelected: (index: Int) -> Unit = {}
) {
    FieldSwitch(
        name = R.string.register_bruxism_pain_switch,
        checked = isInPain,
        onCheckedChange = { onPainChanged(it) }
    )

    FieldSpacer()

    AnimatedVisibility(visible = isInPain) {
        Column {
            Card(elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )) {
                LevelSlider(
                    modifier = Modifier.cardPadding(),
                    titleRes = R.string.register_bruxism_label_pain_level,
                    resultLevelText = stringResource(
                        id = R.string.register_bruxism_label_pain_level_result,
                        painLevel
                    ),
                    levelValue = painLevel,
                    onLevelChange = {
                        onPainLevelChanged(it)
                    }
                )
            }

            FieldSpacer()

            Card(elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )) {
                ImageGridWithCheckboxes(
                    modifier = Modifier.cardPadding(),
                    selectableImages = selectableImages,
                    onImageSelectionChanged = onImageSelected
                )
            }
        }
    }
}

@Preview
@Composable
private fun PainFormPreview() {
    BruxismHelperTheme {
        PainForm(
            selectableImages = mockSelectableImage
        )
    }
}
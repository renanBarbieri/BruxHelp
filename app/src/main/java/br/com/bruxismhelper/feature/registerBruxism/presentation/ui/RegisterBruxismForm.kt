package br.com.bruxismhelper.feature.registerBruxism.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.bruxismhelper.R
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.BottomLeftIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.BottomRightIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.SelectableImage
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.TopLeftIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.TopRightIdentifier
import br.com.bruxismhelper.ui.common.FieldSpacer
import br.com.bruxismhelper.ui.common.FieldSwitch
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme

@Composable
fun RegisterBruxismForm(
    modifier: Modifier = Modifier,
    appBarTitle: MutableIntState = mutableIntStateOf(R.string.register_bruxism_title),
    activityOptions: Array<String> = stringArrayResource(id = R.array.register_bruxism_activity),
    onActivityRegistrationFinished: () -> Unit = {},
) {
    appBarTitle.intValue = R.string.register_bruxism_title

    var isEating by remember { mutableStateOf(false) }
    val selectedActivity = remember { mutableStateOf("") }
    val isInPain = remember { mutableStateOf(false) }
    val painLevel = remember { mutableIntStateOf(0) }
    val stressLevel = remember { mutableIntStateOf(0) }
    val anxietyLevel = remember { mutableIntStateOf(0) }
    val selectableImagesState = remember {
        mutableStateOf(
            listOf(
                SelectableImage(
                    id = TopLeftIdentifier,
                    isSelected = false,
                    imageRes = R.drawable.pain_test
                ),
                SelectableImage(
                    id = TopRightIdentifier,
                    isSelected = false,
                    imageRes = R.drawable.pain_test
                ),
                SelectableImage(
                    id = BottomLeftIdentifier,
                    isSelected = false,
                    imageRes = R.drawable.pain_test
                ),
                SelectableImage(
                    id = BottomRightIdentifier,
                    isSelected = false,
                    imageRes = R.drawable.pain_test
                ),
            )
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = RegisterBruxismDefaults.fieldsOutsidePadding,
                end = RegisterBruxismDefaults.fieldsOutsidePadding,
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            FieldSwitch(
                name = R.string.register_bruxism_eating_switch,
                checked = isEating,
                onCheckedChange = { isEating = it }
            )

            FieldSpacer()
        }

        item {
            AnimatedVisibility(visible = isEating.not()) {
                Column {
                    ActivityTypeDropdown(activityOptions, selectedActivity)
                    FieldSpacer()
                    PainForm(selectableImagesState, isInPain, painLevel, stressLevel, anxietyLevel)
                }
            }

            Spacer(modifier = Modifier.height(RegisterBruxismDefaults.fieldsOutsidePadding))
        }

        item {
            Button(
                onClick = {
                    /* Handle form submission */
                    //TODO submitForm
                    onActivityRegistrationFinished()
                },
            ) {
                Text(stringResource(id = R.string.register_bruxism_send_button))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterBruxismPrev() {
    BruxismHelperTheme {
        RegisterBruxismForm()
    }
}
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.bruxismhelper.R
import br.com.bruxismhelper.feature.registerBruxism.presentation.RegisterBruxismViewModel
import br.com.bruxismhelper.ui.common.FieldSpacer
import br.com.bruxismhelper.ui.common.FieldSwitch
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme

@Composable
fun RegisterBruxismForm(
    modifier: Modifier = Modifier,
    viewModel: RegisterBruxismViewModel = hiltViewModel(),
    appBarTitle: MutableIntState = mutableIntStateOf(R.string.register_bruxism_title),
    activityOptions: Array<String> = stringArrayResource(id = R.array.register_bruxism_activity),
    onActivityRegistrationFinished: () -> Unit = {},
) {
    // Setting the app bar title
    appBarTitle.intValue = R.string.register_bruxism_title

    // Observing the state from ViewModel
    val viewState by viewModel.viewState.collectAsState()

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
                checked = viewState.isEating,
                onCheckedChange = { viewModel.updateIsEating(it) }
            )

            FieldSpacer()
        }

        item {
            AnimatedVisibility(visible = !viewState.isEating) {
                Column {
                    ActivityTypeDropdown(
                        activityOptions = activityOptions,
                        selectedActivity = viewState.selectedActivity,
                        onActivitySelected = { viewModel.updateSelectedActivity(it) }
                    )
                    FieldSpacer()
                    PainForm(
                        isInPain = viewState.isInPain,
                        onPainChanged = { viewModel.updateIsInPain(it) },
                        painLevel = viewState.painLevel,
                        onPainLevelChanged = { viewModel.updatePainLevel(it) },
                        stressLevel = viewState.stressLevel,
                        onStressLevelChanged = { viewModel.updateStressLevel(it) },
                        anxietyLevel = viewState.anxietyLevel,
                        onAnxietyLevelChanged = { viewModel.updateAnxietyLevel(it) },
                        selectableImages = viewState.selectableImages,
                        onImageSelected = { viewModel.updateSelectableImageCheck(it) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(RegisterBruxismDefaults.fieldsOutsidePadding))
        }

        item {
            Button(
                onClick = {
                    // Handle form submission
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
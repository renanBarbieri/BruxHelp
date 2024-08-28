package br.com.bruxismhelper.feature.registerBruxism.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.bruxismhelper.R
import br.com.bruxismhelper.feature.idle.ui.IdleScreen
import br.com.bruxismhelper.feature.idle.ui.loading.LoadingIcon
import br.com.bruxismhelper.feature.registerBruxism.presentation.RegisterBruxismViewModel
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.RegisterBruxismViewState
import br.com.bruxismhelper.feature.registerBruxism.presentation.ui.RegisterBruxismDefaults.cardPadding
import br.com.bruxismhelper.platform.kotlin.whenTrue
import br.com.bruxismhelper.shared.presentation.ui.AlertError
import br.com.bruxismhelper.ui.common.FieldSpacer
import br.com.bruxismhelper.ui.common.FieldSwitch
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme

@Composable
fun RegisterBruxismForm(
    modifier: Modifier = Modifier,
    appBarTitle: MutableIntState = mutableIntStateOf(R.string.register_bruxism_title),
    viewModel: RegisterBruxismViewModel = hiltViewModel(),
    activityOptions: Array<String> = stringArrayResource(id = R.array.register_bruxism_activity),
    onActivityRegistrationFinished: () -> Unit = {},
) {
    // Setting the app bar title
    appBarTitle.intValue = R.string.register_bruxism_title

    // Observing the state from ViewModel
    val viewState by viewModel.viewState.collectAsState()

    Box {
        FormView(
            viewState = viewState,
            activityOptions = activityOptions,
            onIsEatingChanged = { viewModel.updateIsEating(it) },
            onBruxismActivitySelected = { viewModel.updateSelectedActivity(it) },
            onStressLevelUpdated = { viewModel.updateStressLevel(it) },
            onAnxietyLevelUpdated = { viewModel.updateAnxietyLevel(it) },
            onIsInPainChanged = { viewModel.updateIsInPain(it) },
            onPainLevelUpdated = { viewModel.updatePainLevel(it) },
            onPainImageSelected = { viewModel.updateSelectableImageCheck(it) },
            onSubmitFormClick = { viewModel.submitForm() },
        )

        viewState.formSubmitResult?.onSuccess {
            onActivityRegistrationFinished()
        }?.onFailure {
            it.AlertError(
                title = stringResource(id = R.string.register_bruxism_error_title),
                productionText = stringResource(id = R.string.register_bruxism_error_message),
                confirmButtonText = stringResource(id = R.string.register_bruxism_error_confirm_text),
                onConfirmRequest = { viewModel.submitForm() },
                onDismissRequest = { viewModel.onCloseAlertRequest() }
            )
        }

        viewState.showLoading.whenTrue {
            IdleScreen(
                centerIcon = { LoadingIcon() },
                messageStringRes = R.string.loading,
                backgroundColor = MaterialTheme.colorScheme.surfaceBright
            )
        }
    }

}

@Composable
private fun FormView(
    modifier: Modifier = Modifier,
    viewState: RegisterBruxismViewState = RegisterBruxismViewState(),
    activityOptions: Array<String> = stringArrayResource(id = R.array.register_bruxism_activity),
    onIsEatingChanged: (isEating: Boolean) -> Unit = {},
    onBruxismActivitySelected: (activity: String) -> Unit = {},
    onStressLevelUpdated: (level: Int) -> Unit = {},
    onAnxietyLevelUpdated: (level: Int) -> Unit = {},
    onIsInPainChanged: (isEating: Boolean) -> Unit = {},
    onPainLevelUpdated: (level: Int) -> Unit = {},
    onPainImageSelected: (index: Int) -> Unit = {},
    onSubmitFormClick: () -> Unit = {},
) {
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
                checked = viewState.registerBruxismForm.isEating,
                onCheckedChange = { onIsEatingChanged(it) }
            )

            FieldSpacer()
        }

        item {
            AnimatedVisibility(visible = !viewState.registerBruxismForm.isEating) {
                Column {
                    ActivityTypeDropdown(
                        activityOptions = activityOptions,
                        selectedActivity = viewState.registerBruxismForm.selectedActivity,
                        onActivitySelected = { onBruxismActivitySelected(it) }
                    )
                    FieldSpacer()

                    Card(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {
                        LevelSlider(
                            modifier = Modifier.cardPadding(),
                            titleRes = R.string.register_bruxism_label_stress_level,
                            resultLevelText = when (viewState.registerBruxismForm.stressLevel) {
                                in 0..3 -> stringResource(id = R.string.register_bruxism_label_stress_level_low)
                                in 4..7 -> stringResource(id = R.string.register_bruxism_label_stress_level_medium)
                                else -> stringResource(id = R.string.register_bruxism_label_stress_level_high)
                            },
                            lowLevelIconRes = R.drawable.smile_friendly,
                            highLevelIconRes = R.drawable.smile_stress,
                            levelValue = viewState.registerBruxismForm.stressLevel,
                            onLevelChange = {
                                onStressLevelUpdated(it)
                            }
                        )
                    }

                    FieldSpacer()

                    Card(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {
                        LevelSlider(
                            modifier = Modifier.cardPadding(),
                            titleRes = R.string.register_bruxism_label_anxiety_level,
                            resultLevelText = when (viewState.registerBruxismForm.anxietyLevel) {
                                in 0..3 -> stringResource(id = R.string.register_bruxism_label_anxiety_level_low)
                                in 4..7 -> stringResource(id = R.string.register_bruxism_label_anxiety_level_medium)
                                else -> stringResource(id = R.string.register_bruxism_label_anxiety_level_high)
                            },
                            lowLevelIconRes = R.drawable.smile_friendly,
                            highLevelIconRes = R.drawable.smile_stress,
                            levelValue = viewState.registerBruxismForm.anxietyLevel,
                            onLevelChange = {
                                onAnxietyLevelUpdated(it)
                            }
                        )
                    }
                    FieldSpacer()
                    PainForm(
                        isInPain = viewState.registerBruxismForm.isInPain,
                        onPainChanged = { onIsInPainChanged(it) },
                        painLevel = viewState.registerBruxismForm.painLevel,
                        onPainLevelChanged = { onPainLevelUpdated(it) },
                        selectableImages = viewState.registerBruxismForm.selectableImages,
                        onImageSelected = { onPainImageSelected(it) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(RegisterBruxismDefaults.fieldsOutsidePadding))
        }

        item {
            Button(
                enabled = viewState.allMandatoryFieldsFilled,
                onClick = onSubmitFormClick
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
        FormView()
    }
}
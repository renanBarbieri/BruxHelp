package br.com.bruxismhelper.feature.register.presentation.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.bruxismhelper.R
import br.com.bruxismhelper.emptyString
import br.com.bruxismhelper.feature.idle.ui.IdleScreen
import br.com.bruxismhelper.feature.idle.ui.loading.LoadingIcon
import br.com.bruxismhelper.feature.register.domain.model.Dentist
import br.com.bruxismhelper.feature.register.presentation.RegisterViewModel
import br.com.bruxismhelper.feature.register.presentation.model.FrequencyViewObject
import br.com.bruxismhelper.feature.register.presentation.model.OralHabitViewObject
import br.com.bruxismhelper.feature.register.presentation.model.RegisterFields
import br.com.bruxismhelper.feature.register.presentation.model.RegisterViewState
import br.com.bruxismhelper.platform.kotlin.whenTrue
import br.com.bruxismhelper.shared.presentation.ui.AlertError
import br.com.bruxismhelper.ui.common.spacerField
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme

@SuppressLint("InlinedApi")
@Composable
fun RegisterForm(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegistrationFinished: () -> Unit = {},
    onRegistrationIgnored: () -> Unit = {},
) {
    val formState by viewModel.viewState.collectAsState()

    if (formState.submitSuccess) {
        onRegistrationFinished()
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        FormView(
            modifier = modifier,
            viewState = formState,
            onNameChanged = { viewModel.updateFullName(it) },
            onEmailChanged = { viewModel.updateEmail(it) },
            onDoctorChanged = { viewModel.updateDentist(it) },
            onMedicinesChanged = { viewModel.updateContinuousMedicines(it) },
            onCaffeineQuantityChanged = { viewModel.updateCaffeineConsumptionQuantity(it) },
            onCaffeineFrequencyChanged = { viewModel.updateCaffeineConsumptionFrequency(it) },
            onSmokingQuantityChanged = { viewModel.updateSmokingQuantity(it) },
            onSmokingFrequencyChanged = { viewModel.updateSmokingFrequency(it) },
            onHabitChanged = { habit, checked ->
                viewModel.updateOralHabits(habit, checked)
            },
            onFormSubmit = { viewModel.submitForm() },
            onFormIgnored = {
                viewModel.ignoreForm()
                onRegistrationIgnored()
            },
        )

        formState.error?.AlertError(
            title = stringResource(id = R.string.register_error_title),
            productionText = stringResource(id = R.string.register_error_message),
            confirmButtonText = stringResource(id = R.string.register_error_confirm_text),
            onConfirmRequest = { viewModel.submitForm() },
            onDismissRequest = { viewModel.onCloseAlertRequest() }
        )

        formState.showLoading.whenTrue {
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
    viewState: RegisterViewState,
    onNameChanged: (String) -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
    onDoctorChanged: (String) -> Unit = {},
    onMedicinesChanged: (String) -> Unit = {},
    onCaffeineQuantityChanged: (String) -> Unit = {},
    onCaffeineFrequencyChanged: (String) -> Unit = {},
    onSmokingQuantityChanged: (String) -> Unit = {},
    onSmokingFrequencyChanged: (String) -> Unit = {},
    onHabitChanged: (OralHabitViewObject, Boolean) -> Unit = { _, _ -> },
    onFormIgnored: () -> Unit = {},
    onFormSubmit: () -> Unit = {},
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            OutlinedTextField(
                value = viewState.registerForm.fullName,
                onValueChange = { onNameChanged(it) },
                label = { Text(stringResource(id = R.string.register_label_name)) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        spacerField()

        item {
            OutlinedTextField(
                value = viewState.registerForm.email,
                onValueChange = { onEmailChanged(it) },
                label = { Text(stringResource(id = R.string.register_label_email)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                )
            )
        }

        spacerField()

//        item {
//            FieldDropdown(
//                selectedOption = viewState.registerForm.dentist.name,
//                label = R.string.register_label_doctor,
//                options = viewState.formFields.dentists.map { it.name }.toTypedArray(),
//                onOptionSelected = {
//                    onDoctorChanged(it)
//                }
//            )
//        }

        spacerField()

        item {
            OutlinedTextField(
                value = viewState.registerForm.continuousMedicines,
                onValueChange = { onMedicinesChanged(it) },
                label = { Text(stringResource(id = R.string.register_label_medicine)) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        spacerField()

        item {
            OutlinedTextField(
                value = viewState.registerForm.caffeineConsumption.quantity.toStringOrEmpty(),
                onValueChange = { onCaffeineQuantityChanged(it) },
                label = { Text(stringResource(id = R.string.register_label_caffeine_quantity)) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        spacerField()

        item {
            FieldDropdown(
                selectedOption = stringResource(id = viewState.registerForm.caffeineConsumption.frequencyToResStringOrEmpty()),
                label = R.string.register_label_caffeine_frequency,
                options = FrequencyViewObject.entries.map { stringResource(id = it.fieldNameRes) }
                    .toTypedArray(),
                onOptionSelected = {
                    onCaffeineFrequencyChanged(it)
                }
            )
        }

        spacerField()

        item {
            OutlinedTextField(
                value = viewState.registerForm.smoking.quantity.toStringOrEmpty(),
                onValueChange = { onSmokingQuantityChanged(it) },
                label = { Text(stringResource(id = R.string.register_label_smoking_quantity)) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        spacerField()

        item {
            FieldDropdown(
                selectedOption = stringResource(id = viewState.registerForm.smoking.frequencyToResStringOrEmpty()),
                label = R.string.register_label_smoking_frequency,
                options = FrequencyViewObject.entries.map { stringResource(id = it.fieldNameRes) }
                    .toTypedArray(),
                onOptionSelected = {
                    onSmokingFrequencyChanged(it)
                }
            )
        }

        spacerField()

        item {
            Text(text = stringResource(id = R.string.register_label_habit))
        }

        item {
            Column {
                OralHabitViewObject.entries.forEach { habit ->
                    val isSelected = viewState.registerForm.oralHabits.contains(habit)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { isChecked -> onHabitChanged(habit, isChecked) }
                        )
                        Text(text = stringResource(id = habit.fieldNameRes))
                    }
                }
            }
        }

        spacerField()

        item {
            Row {
//                IgnoreButton(
//                    modifier = Modifier.align(Alignment.CenterVertically),
//                    onRegistrationIgnored = { onFormIgnored() }
//                )
//
//                Spacer(modifier = Modifier.weight(1f))

                Button(
                    enabled = viewState.allMandatoryFieldsFilled,
                    onClick = {
                        onFormSubmit()
                    },
                ) {
                    Text(stringResource(id = R.string.register_button))
                }
            }
        }

        spacerField()
    }
}

private fun Int?.toStringOrEmpty(): String = this?.toString() ?: emptyString()

@Composable
private fun IgnoreButton(
    modifier: Modifier = Modifier,
    onRegistrationIgnored: () -> Unit
) {
    val annotatedString: AnnotatedString = buildAnnotatedString {
        val string = stringResource(id = R.string.register_ignore_button)
        append(string)
        addStyle(
            style = androidx.compose.ui.text.SpanStyle(
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.primary
            ),
            start = 0,
            end = string.length
        )
    }

    Text(
        text = annotatedString,
        modifier = modifier.clickable { onRegistrationIgnored() },
        style = MaterialTheme.typography.bodyLarge,
    )

}

@Preview(showBackground = true)
@Composable
private fun RegisterPreview() {
    val dentists = listOf(Dentist(name = "Dra. Nathália Celestino"), Dentist(name = "Outro"))
    BruxismHelperTheme {
        FormView(
            viewState = RegisterViewState(
                formFields = RegisterFields(dentists)
            )
        )
    }
}
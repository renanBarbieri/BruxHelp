package br.com.bruxismhelper.feature.register.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.bruxismhelper.R
import br.com.bruxismhelper.emptyString
import br.com.bruxismhelper.feature.register.domain.model.Dentist
import br.com.bruxismhelper.feature.register.presentation.RegisterViewModel
import br.com.bruxismhelper.feature.register.presentation.model.FrequencyViewObject
import br.com.bruxismhelper.feature.register.presentation.model.OralHabitViewObject
import br.com.bruxismhelper.feature.register.presentation.model.RegisterFields
import br.com.bruxismhelper.feature.register.presentation.model.RegisterFormField
import br.com.bruxismhelper.feature.register.presentation.model.RegisterViewState
import br.com.bruxismhelper.ui.common.spacerField
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme


@Composable
fun RegisterForm(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
    appBarTitle: MutableIntState = mutableIntStateOf(R.string.register_title),
    onRegistrationFinished: () -> Unit = {},
    onRegistrationIgnored: () -> Unit = {},
) {
    appBarTitle.intValue = R.string.register_title

    val formState by viewModel::viewState

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormView(
            modifier = modifier,
            viewState = formState,
            onNameChanged = { viewModel.updateField(RegisterFormField.FULL_NAME, it) },
            onEmailChanged = { viewModel.updateField(RegisterFormField.EMAIL, it) },
            onDoctorChanged = { viewModel.updateField(RegisterFormField.DENTIST, it) },
            onMedicinesChanged = { viewModel.updateField(RegisterFormField.DENTIST, it) },
            onCaffeineQuantityChanged = { viewModel.updateField(RegisterFormField.CAFFEINE_CONSUMPTION_QUANTITY, it) },
            onCaffeineFrequencyChanged = { viewModel.updateField(RegisterFormField.CAFFEINE_CONSUMPTION_FREQUENCY, it) },
            onSmokingQuantityChanged = { viewModel.updateField(RegisterFormField.SMOKING_QUANTITY, it) },
            onSmokingFrequencyChanged = { viewModel.updateField(RegisterFormField.SMOKING_FREQUENCY, it) },
            onHabitChanged = { habit, checked ->
                viewModel.updateField(RegisterFormField.ORAL_HABITS, Pair(habit, checked))
            },
            onFormSubmit = { viewModel.submitForm() },
            onFormIgnored = { onRegistrationIgnored() },
        )
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

        item {
            FieldDropdown(
                selectedOption = viewState.registerForm.dentist.name,
                label = R.string.register_label_doctor,
                options = viewState.formFields.dentists.map { it.name }.toTypedArray(),
                onOptionSelected = {
                    onDoctorChanged(it)
                }
            )
        }

        spacerField()

        item {
            OutlinedTextField(
                value = viewState.registerForm.continuousMedications,
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
                options = FrequencyViewObject.entries.map { stringResource(id = it.fieldNameRes) }.toTypedArray(),
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
                options = FrequencyViewObject.entries.map { stringResource(id = it.fieldNameRes) }.toTypedArray(),
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
                IgnoreButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onRegistrationIgnored = { onFormIgnored() }
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        onFormSubmit()
                    },
                ) {
                    Text(stringResource(id = R.string.register_button))
                }
            }
        }
    }
}

private fun Int?.toStringOrEmpty(): String = this?.toString() ?: emptyString()

@Composable
private fun IgnoreButton(
    modifier: Modifier = Modifier,
    onRegistrationIgnored: () -> Unit
) {
    val annotatedString = buildAnnotatedString {
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

    ClickableText(
        modifier = modifier,
        text = annotatedString,
        onClick = {
            onRegistrationIgnored()
        },
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
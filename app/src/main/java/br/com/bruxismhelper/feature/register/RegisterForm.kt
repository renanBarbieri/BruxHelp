package br.com.bruxismhelper.feature.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import br.com.bruxismhelper.R
import br.com.bruxismhelper.emptyString
import br.com.bruxismhelper.ui.common.FieldSpacer
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme

@Composable
fun RegisterForm(
    modifier: Modifier = Modifier,
    appBarTitle: MutableIntState = mutableIntStateOf(R.string.register_title),
    onRegistrationFinished: () -> Unit = {},
    onRegistrationIgnored: () -> Unit = {},
) {
    appBarTitle.intValue = R.string.register_title

    var fullName by remember { mutableStateOf(emptyString()) }
    var email by remember { mutableStateOf(emptyString()) }
    val selectedDoctor = remember { mutableStateOf(emptyString()) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text(stringResource(id = R.string.register_label_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        FieldSpacer()

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(id = R.string.register_label_email)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
        )

        FieldSpacer()

        DoctorDropdown(selectedDoctor = selectedDoctor)

        Spacer(modifier = Modifier.height(RegisterDefaults.fieldsOutsidePadding))

        Row {
            IgnoreButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onRegistrationIgnored = { onRegistrationIgnored() }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    /* Handle form submission */
                    //TODO submitForm
                    onRegistrationFinished()
                },
            ) {
                Text(stringResource(id = R.string.register_button))
            }
        }

    }
}

@Composable
fun IgnoreButton(
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
    BruxismHelperTheme {
        RegisterForm()
    }
}
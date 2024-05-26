package br.com.bruxismhelper.feature.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import br.com.bruxismhelper.R
import br.com.bruxismhelper.emptyString
import br.com.bruxismhelper.ui.common.FieldSpacer
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme

@Composable
fun RegisterForm(modifier: Modifier = Modifier) {
    var fullName by remember { mutableStateOf(emptyString()) }
    var email by remember { mutableStateOf(emptyString()) }
    val selectedDoctor = remember { mutableStateOf(emptyString()) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.register_title),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = RegisterDefaults.fieldsOutsidePadding)
        )

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

        Button(
            onClick = { /* Handle form submission */ },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(id = R.string.register_button))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterPreview() {
    BruxismHelperTheme {
        RegisterForm()
    }
}
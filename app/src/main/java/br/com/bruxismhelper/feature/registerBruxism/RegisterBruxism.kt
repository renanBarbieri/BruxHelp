package br.com.bruxismhelper.feature.registerBruxism

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import br.com.bruxismhelper.ui.common.FieldSpacer
import br.com.bruxismhelper.ui.common.FieldSwitch
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme

@Composable
fun RegisterBruxism(
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(RegisterBruxismDefaults.fieldsOutsidePadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FieldSwitch(
            name = R.string.register_bruxism_eating_switch,
            checked = isEating,
            onCheckedChange = { isEating = it }
        )

        FieldSpacer()

        if (!isEating) {
            NotEatingForm(activityOptions, selectedActivity)
            FieldSpacer()
            PainForm(isInPain, painLevel)
        }

        Spacer(modifier = Modifier.height(RegisterBruxismDefaults.fieldsOutsidePadding))

        Button(
            onClick = {
                /* Handle form submission */
                //TODO submitForm
                onActivityRegistrationFinished()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(id = R.string.register_bruxism_send_button))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterBruxismPrev() {
    BruxismHelperTheme {
        RegisterBruxism()
    }
}
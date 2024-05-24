package br.com.bruxismhelper.feature.registerBruxism

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.bruxismhelper.R
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterBruxism(
    activityOptions: Array<String> = stringArrayResource(id = R.array.register_bruxism_activity)
) {
    var isEating by remember { mutableStateOf(false) }
    var selectedActivity by remember { mutableStateOf("Selecione uma atividade") }
    var isInPain by remember { mutableStateOf(false) }
    var painLevel by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Formulário de Atividade",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Estou em refeição
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Estou em refeição: ")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = isEating, onCheckedChange = { isEating = it })
        }

        // Lista de tipos de atividade (aparece apenas se não estiver em refeição)
        if (!isEating) {
            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedActivity,
                    onValueChange = { selectedActivity = it },
                    label = { Text("Tipo de Atividade") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    activityOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedActivity = option
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Estou sentindo dor
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Estou sentindo dor: ")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(checked = isInPain, onCheckedChange = { isInPain = it })
            }

            // Grau da dor (aparece apenas se estiver sentindo dor)
            if (isInPain) {
                Text("Grau da dor (0 a 10):")
                Slider(
                    value = painLevel.toFloat(),
                    onValueChange = { painLevel = it.toInt() },
                    valueRange = 0f..10f,
                    steps = 9,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(text = "Nível de dor: $painLevel", modifier = Modifier.align(Alignment.Start))
            }

        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Handle form submission */ },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Enviar")
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
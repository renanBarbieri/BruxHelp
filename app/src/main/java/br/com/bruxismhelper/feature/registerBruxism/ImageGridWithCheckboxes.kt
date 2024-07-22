package br.com.bruxismhelper.feature.registerBruxism

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.bruxismhelper.R
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme

@Composable
fun ImageGridWithCheckboxes(images: List<Int>) {
    //TODO observar as mudanças de estado dos itens selecionados
    val checkedStates = remember { mutableStateListOf(false, false, false, false) }

    Column {
        Text(stringResource(id = R.string.register_bruxism_label_pain_region))

        images.chunked(2).forEachIndexed { rowIndex, rowImages ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowImages.forEachIndexed { colIndex, imageRes ->
                    val index = rowIndex * 2 + colIndex
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(8.dp)
                            .clickable {
                                checkedStates[index] = !checkedStates[index]
                            },
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp)),
                        )
                        Checkbox(
                            checked = checkedStates[index],
                            onCheckedChange = { checkedStates[index] = it },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun GridPreview() {
    BruxismHelperTheme {
        ImageGridWithCheckboxes(
            listOf(
                R.drawable.pain_test,
                R.drawable.pain_test,
                R.drawable.pain_test,
                R.drawable.pain_test,
            )
        )
    }
}
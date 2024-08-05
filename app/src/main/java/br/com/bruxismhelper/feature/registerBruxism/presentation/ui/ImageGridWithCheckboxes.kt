package br.com.bruxismhelper.feature.registerBruxism.presentation.ui

import android.annotation.SuppressLint
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.bruxismhelper.R
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.AtmLeftIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.AtmRightIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.Identifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.MasseterLeftIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.MasseterRightIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.SelectableImage
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.TemporalLeftIdentifier
import br.com.bruxismhelper.feature.registerBruxism.presentation.model.TemporalRightIdentifier
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme

@Composable
fun ImageGridWithCheckboxes(
    modifier: Modifier = Modifier,
    selectableImages: List<SelectableImage> = listOf(),
    onImageSelectionChanged: (index: Int) -> Unit = {}
) {
    Column(modifier = modifier) {
        Text(
            fontWeight = FontWeight.Bold,
            text = stringResource(id = R.string.register_bruxism_label_pain_region)
        )

        selectableImages.chunked(2).forEachIndexed { rowIndex, rowImages ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowImages.forEachIndexed { colIndex, selectableImage ->
                    val index = rowIndex * 2 + colIndex
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(8.dp)
                            .clickable {
                                onImageSelectionChanged(index)
//                                val updatedImages = selectableImages
//                                    .toMutableList().apply {
//                                        this[index] = this[index].copy(isSelected = !this[index].isSelected)
//                                    }
//                                selectableImages.value = updatedImages
                            },
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Image(
                            painter = painterResource(id = selectableImage.imageRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp)),
                        )
                        Checkbox(
                            checked = selectableImage.isSelected,
                            onCheckedChange = {
                                onImageSelectionChanged(index)
//                                val updatedImages = selectableImages.toMutableList().apply {
//                                    this[index] = this[index].copy(isSelected = it)
//                                }
//                                selectableImages.value = updatedImages
                            },
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun GridPreview() {
    BruxismHelperTheme {
        ImageGridWithCheckboxes(
            selectableImages = listOf(
                SelectableImage(
                    id = TemporalLeftIdentifier,
                    isSelected = false,
                    imageRes = R.drawable.pain_test
                ),
                SelectableImage(
                    id = TemporalRightIdentifier,
                    isSelected = false,
                    imageRes = R.drawable.pain_test
                ),
                SelectableImage(
                    id = MasseterLeftIdentifier,
                    isSelected = false,
                    imageRes = R.drawable.pain_test
                ),
                SelectableImage(
                    id = MasseterRightIdentifier,
                    isSelected = false,
                    imageRes = R.drawable.pain_test
                ),
                SelectableImage(
                    id = AtmLeftIdentifier,
                    isSelected = false,
                    imageRes = R.drawable.pain_test
                ),
                SelectableImage(
                    id = AtmRightIdentifier,
                    isSelected = false,
                    imageRes = R.drawable.pain_test
                ),
            ),
        )
    }
}

package br.com.bruxismhelper.feature.idle.ui.waiting

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Suppress("ConstPropertyName")
internal object WaitingDefaults {
    val outsideMaxSize = 200.dp
    val insideMaxSize = 150.dp
    val outsideMinSize = 50.dp
    val insideMinSize = 37.dp
    const val zeroDegree = 0f

    const val animationDelay: Long = 2000
    const val insideCircleDuration: Int = 1600
    const val outsideCircleDuration: Int = 2000
    const val rotationAngle: Float = 180f
    const val rotationDuration: Int = 1250

    val waitingTextFontSize = 20.sp
}
package br.com.bruxismhelper.feature.waiting.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.bruxismhelper.R
import br.com.bruxismhelper.feature.waiting.ui.WaitingDefaults.insideMaxSize
import br.com.bruxismhelper.feature.waiting.ui.WaitingDefaults.insideMinSize
import br.com.bruxismhelper.feature.waiting.ui.WaitingDefaults.outsideMaxSize
import br.com.bruxismhelper.feature.waiting.ui.WaitingDefaults.outsideMinSize
import br.com.bruxismhelper.feature.waiting.ui.WaitingDefaults.zeroDegree
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme
import kotlinx.coroutines.delay

@Composable
fun WaitingIcon(
    animationDelay: Long = WaitingDefaults.animationDelay,
    insideCircleDuration: Int = WaitingDefaults.insideCircleDuration,
    outsideCircleDuration: Int = WaitingDefaults.outsideCircleDuration,
    rotationAngle: Float = WaitingDefaults.rotationAngle,
    rotationDuration: Int = WaitingDefaults.rotationDuration,
) {
    var isExpanded by remember { mutableStateOf(true) }
    var rememberRotationAngle by remember { mutableFloatStateOf(zeroDegree) }

    val insideCircleSize by animateDpAsState(
        targetValue = if (isExpanded) insideMaxSize else insideMinSize,
        animationSpec = tween(durationMillis = insideCircleDuration),
        label = "Inside circle resizing"
    )

    val outsideCircleSize by animateDpAsState(
        targetValue = if (isExpanded) outsideMaxSize else outsideMinSize,
        animationSpec = tween(durationMillis = outsideCircleDuration),
        label = "Outside circle resizing"
    )

    val animatedRotation by animateFloatAsState(
        targetValue = rememberRotationAngle,
        animationSpec = tween(durationMillis = rotationDuration),
        label = "Image rotation"
    )

    // Loop the animation
    LaunchedEffect(Unit) {
        while (true) {
            isExpanded = !isExpanded
            rememberRotationAngle =
                if (rememberRotationAngle == zeroDegree) rotationAngle else zeroDegree
            delay(animationDelay)
        }
    }

    Box {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(outsideCircleSize)
                .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(insideCircleSize)
                .background(MaterialTheme.colorScheme.onPrimaryContainer, shape = CircleShape)
        )
        Image(
            modifier = Modifier
                .rotate(animatedRotation)
                .size(insideCircleSize)
                .align(Alignment.Center),
            painter = painterResource(id = R.drawable.waiting_icon),
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
    }
}

@Preview
@Composable
private fun PreviewWaitingIcon() {
    BruxismHelperTheme {
        WaitingIcon()
    }
}
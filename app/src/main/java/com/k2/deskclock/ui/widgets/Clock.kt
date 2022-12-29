package com.k2.deskclock.ui.widgets

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.k2.deskclock.ui.UiStateHolder
import com.k2.deskclock.utils.getFormattedTime

@Composable
fun Clock(uiStates: UiStateHolder) {
    val hour: MutableState<String> = remember { mutableStateOf("") }
    val min: MutableState<String> = remember { mutableStateOf("") }
    val amOrPm: MutableState<String> = remember { mutableStateOf("") }

    uiStates.time.run {
        hour.value = this.value.getFormattedTime("h")
        min.value = this.value.getFormattedTime("mm")
        amOrPm.value = this.value.getFormattedTime("a")
    }

    val separatorAlpha by rememberInfiniteTransition().animateFloat(
        initialValue = 0.0F,
        targetValue = 1.0F,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = { fraction ->
                if (fraction > 0.9F) 1.0F else fraction
            }),
            repeatMode = RepeatMode.Reverse
        )
    )

    Text(
        buildAnnotatedString {
            append(hour.value)
            withStyle(style = SpanStyle(color = colors.onBackground.copy(separatorAlpha))) {
                append(":")
            }
            append(min.value)
            withStyle(style = SpanStyle(fontSize = 1.sp)) {
                append(" ")
            }
            withStyle(style = SpanStyle(fontSize = (uiStates.clockSize.value / 2).sp)) {
                append(amOrPm.value)
            }
        },
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        fontSize = uiStates.clockSize.value.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 4.sp
    )
}

package com.k2.deskclock.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.k2.deskclock.R
import com.k2.deskclock.location.models.Place
import com.k2.deskclock.ui.UiStateHolder
import com.k2.deskclock.utils.getFormattedTime
import com.k2.deskclock.utils.launchUrl
import com.k2.deskclock.wallpaper.data.models.Wallpaper

private const val TAG_PHOTOGRAPHER = "photographer"
private const val TAG_SOURCE = "website"

@Composable
fun HomeView(
    uiStates: UiStateHolder,
    ambientClick: () -> Unit,
    navController: NavController,
    refreshClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { ambientClick.invoke() }
    ) {
        uiStates.wallpaper.value?.let { wallpaper ->
            AsyncImage(
                model = wallpaper.path,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background.copy(alpha = 0.5F))
        )
        if (uiStates.insetVisible.value) {
            NavIcon(
                navController = navController,
                icon = R.drawable.ic_ambient,
                alignment = Alignment.TopStart,
                navTarget = "ambient"
            )
            NavIcon(
                navController = navController,
                icon = R.drawable.ic_settings,
                alignment = Alignment.TopEnd,
                navTarget = "settings",
                clearBackStack = false
            )
            uiStates.wallpaper.value?.let { wallpaper ->
                ImageAnnotation(wallpaper)
            }
            Image(
                painter = painterResource(R.drawable.ic_sync),
                contentDescription = "refresh",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .absoluteOffset(y = (-60).dp)
                    .size(32.dp)
                    .clickable { refreshClick.invoke() }
            )
        } else {
            uiStates.weather.value?.place?.let { PlaceText(location = it) }
            DateText(uiStates)
        }
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Clock(uiStates)
            uiStates.weather.value?.let {
                Weather(weather = it, showForecast = true)
            }
        }
        LocationErrorSnackBar(uiStateHolder = uiStates)
    }
}

@Composable
private fun BoxScope.ImageAnnotation(wallpaper: Wallpaper) {
    val context = LocalContext.current
    val annotatedText = buildAnnotatedString {
        append("Photo by ")
        pushStringAnnotation(
            tag = TAG_PHOTOGRAPHER,
            annotation = wallpaper.credit.userUrl
        )
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(wallpaper.credit.userName)
        }
        pop()
        append(" on ")
        pushStringAnnotation(
            tag = TAG_SOURCE,
            annotation = wallpaper.credit.websiteUrl
        )
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(wallpaper.credit.websiteName)
        }
        pop()
    }

    ClickableText(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(32.dp),
        style = TextStyle.Default.copy(color = MaterialTheme.colors.onBackground),
        text = annotatedText,
        onClick = { offset ->
            annotatedText.getStringAnnotations(
                tag = TAG_PHOTOGRAPHER,
                start = offset,
                end = offset
            ).firstOrNull()?.let { annotation ->
                context.launchUrl(annotation.item)
            }
            annotatedText.getStringAnnotations(
                tag = TAG_SOURCE,
                start = offset,
                end = offset
            ).firstOrNull()?.let { annotation ->
                context.launchUrl(annotation.item)
            }
        }
    )

}

@Composable
fun BoxScope.DateText(uiStates: UiStateHolder) {
    val format = "EEEE, MMMM d"
    StatusText(
        text = uiStates.time.value.getFormattedTime(format),
        alignment = Alignment.TopEnd
    )
}

@Composable
fun BoxScope.PlaceText(location: Place) {
    val place = remember { mutableStateOf(location) }
    StatusText(
        text = place.value.city ?: "",
        alignment = Alignment.TopStart
    )
}

@Composable
fun BoxScope.StatusText(
    text: String,
    alignment: Alignment
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = Modifier
            .alpha(0.85F)
            .align(alignment)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

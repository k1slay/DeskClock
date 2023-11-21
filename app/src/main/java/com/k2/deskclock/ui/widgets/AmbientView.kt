package com.k2.deskclock.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.k2.deskclock.R
import com.k2.deskclock.ui.UiStateHolder

@Composable
fun AmbientView(
    uiStates: UiStateHolder,
    ambientClick: () -> Unit,
    navController: NavController,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .clickable { ambientClick.invoke() },
    ) {
        if (uiStates.insetVisible.value) {
            NavIcon(
                navController = navController,
                icon = R.drawable.ic_sunny,
                alignment = Alignment.TopStart,
                navTarget = "home",
            )
            NavIcon(
                navController = navController,
                icon = R.drawable.ic_settings,
                alignment = Alignment.TopEnd,
                navTarget = "settings",
                clearBackStack = false,
            )
        }
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Clock(uiStates)
            uiStates.weather.value?.let {
                Weather(weather = it, showForecast = false)
            }
        }
        LocationErrorSnackBar(uiStateHolder = uiStates)
    }
}

@Composable
fun BoxScope.NavIcon(
    navController: NavController,
    @DrawableRes icon: Int,
    alignment: Alignment,
    navTarget: String,
    clearBackStack: Boolean = true,
) {
    Image(
        painter = painterResource(icon),
        contentDescription = navTarget,
        colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
        modifier =
            Modifier
                .align(alignment)
                .absoluteOffset(y = 20.dp)
                .padding(20.dp)
                .size(32.dp)
                .alpha(0.8F)
                .clickable {
                    if (clearBackStack) {
                        navController.popBackStack()
                    }
                    navController.navigate(navTarget, navOptions { launchSingleTop = true })
                },
    )
}

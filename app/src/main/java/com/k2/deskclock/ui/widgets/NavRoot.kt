package com.k2.deskclock.ui.widgets

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.k2.deskclock.commons.preferences.PreferenceStore
import com.k2.deskclock.ui.UiStateHolder

@Composable
fun NavRoot(
    uiStateHolder: UiStateHolder,
    preferenceStore: PreferenceStore,
    ambientClick: () -> Unit,
    refreshClick: () -> Unit,
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = preferenceStore.homeMode) {
        composable(NavTarget.Ambient.name) {
            preferenceStore.homeMode = NavTarget.Ambient.name
            uiStateHolder.navState.value = NavTarget.Ambient
            AmbientView(uiStateHolder, ambientClick, navController)
        }
        composable(NavTarget.Settings.name) {
            uiStateHolder.navState.value = NavTarget.Settings
            Settings(uiStateHolder, navController, preferenceStore)
        }
        composable(NavTarget.Home.name) {
            preferenceStore.homeMode = NavTarget.Home.name
            uiStateHolder.navState.value = NavTarget.Home
            HomeView(
                uiStateHolder,
                ambientClick = ambientClick,
                navController = navController,
                refreshClick = refreshClick,
            )
        }
    }
}

sealed class NavTarget(val name: String) {
    object Home : NavTarget("home")

    object Settings : NavTarget("settings")

    object Ambient : NavTarget("ambient")

    val String.fromName: NavTarget
        get() {
            return when (this) {
                Home.name -> Home
                Settings.name -> Settings
                Ambient.name -> Ambient
                else -> throw java.lang.IllegalArgumentException("invalid nav target")
            }
        }
}

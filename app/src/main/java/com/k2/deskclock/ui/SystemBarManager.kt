package com.k2.deskclock.ui

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.compose.runtime.MutableState
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

interface SystemBarManager {
    val activityWindow: Window?

    val insetState: MutableState<Boolean>

    val viewScope: CoroutineScope

    fun toggleSystemBars() {
        val newState = insetState.value.not()
        setSystemBarState(newState)
        insetState.value = newState
    }

    fun setSystemBarState(showSystemBars: Boolean) {
        pendingSystemBarHideJob?.cancel()
        if (needsCompat) {
            setSystemBarStateCompat(showSystemBars)
            return
        }
        val window = activityWindow ?: return
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        windowInsetsController.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        keepScreenOn(showSystemBars.not())
        if (showSystemBars) {
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
            pendingSystemBarHideJob = delayedSystemBarHide()
        } else {
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        }
    }

    private fun setSystemBarStateCompat(showSystemBars: Boolean) {
        val decorView: View = activityWindow?.decorView ?: return
        val flags = if (showSystemBars) SHOW_FLAGS_COMPAT else HIDE_FLAGS_COMPAT
        decorView.systemUiVisibility = flags
        keepScreenOn(showSystemBars.not())
        if (showSystemBars) {
            pendingSystemBarHideJob = delayedSystemBarHide()
        }
    }

    private fun keepScreenOn(screenOn: Boolean) =
        activityWindow?.let {
            if (screenOn) {
                it.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                it.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }

    private fun delayedSystemBarHide() =
        viewScope.launch {
            delay(TimeUnit.SECONDS.toMillis(10))
            setSystemBarState(false)
            insetState.value = false
        }

    private inline val needsCompat: Boolean
        get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.O

    companion object SystemBarFlags {
        private var pendingSystemBarHideJob: Job? = null

        const val SHOW_FLAGS_COMPAT: Int = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        )

        const val HIDE_FLAGS_COMPAT: Int = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
    }
}

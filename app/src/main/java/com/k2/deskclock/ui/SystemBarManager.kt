package com.k2.deskclock.ui

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.compose.runtime.MutableState
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import java.util.concurrent.TimeUnit

interface SystemBarManager {

    val activityWindow: Window?

    val insetState: MutableState<Boolean>

    fun toggleSystemBars() {
        val newState = insetState.value.not()
        setSystemBarState(newState)
        insetState.value = newState
    }

    fun setSystemBarState(showSystemBars: Boolean) {
        if (needsCompat) {
            setSystemBarStateCompat(showSystemBars)
            return
        }
        val decorView: View = activityWindow?.decorView ?: return
        val windowInsetsController = ViewCompat.getWindowInsetsController(decorView) ?: return
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        keepScreenOn(showSystemBars.not())
        if (showSystemBars) {
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
            delayedSystemBarHide(decorView)
        } else {
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        }
    }

    private fun setSystemBarStateCompat(showSystemBars: Boolean) {
        val decorView: View = activityWindow?.decorView ?: return
        val flags = if (showSystemBars) showFlags else hideFlags
        decorView.systemUiVisibility = flags
        keepScreenOn(showSystemBars.not())
        if (showSystemBars) {
            delayedSystemBarHide(decorView)
        }
    }

    private fun keepScreenOn(screenOn: Boolean) = activityWindow?.let {
        if (screenOn) {
            it.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            it.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    private fun delayedSystemBarHide(view: View) {
        view.postDelayed({
            setSystemBarState(false)
            insetState.value = false
        }, TimeUnit.SECONDS.toMillis(10))
    }

    private inline val needsCompat: Boolean
        get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.O

    companion object SystemBarFlags {

        const val showFlags: Int = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            )

        const val hideFlags: Int = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
    }
}

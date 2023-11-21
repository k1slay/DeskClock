package com.k2.deskclock

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.k2.deskclock.location.models.ErrorType
import com.k2.deskclock.location.utils.hasLocationPermission
import com.k2.deskclock.ui.SystemBarManager
import com.k2.deskclock.ui.UiStateHolder.Companion.PERIODIC_REFRESH_INTERVAL
import com.k2.deskclock.ui.theme.DeskClockTheme
import com.k2.deskclock.ui.widgets.NavRoot
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import java.lang.Long.min

@AndroidEntryPoint
class MainActivity : ComponentActivity(), Runnable, SystemBarManager {
    val viewModel by viewModels<DeskClockViewModel>()
    private val tickReceiver = TickReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            DeskClockTheme(viewModel.darkTheme.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    NavRoot(
                        uiStateHolder = viewModel,
                        preferenceStore = viewModel.preferenceStore,
                        ambientClick = { toggleSystemBars() },
                        refreshClick = {
                            cancelRunner()
                            loadAll()
                            scheduleRunner()
                        },
                    )
                }
            }
        }
        if (savedInstanceState == null) {
            toggleSystemBars()
            loadAll()
        } else {
            setSystemBarState(viewModel.insetVisible.value)
        }
    }

    override fun onPause() {
        super.onPause()
        cancelRunner()
        unregisterReceiver(tickReceiver)
    }

    override fun onResume() {
        super.onResume()
        scheduleRunner()
        registerReceiver(tickReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
    }

    private fun checkAndRequestLocationPermission() {
        if (viewModel.weather.value != null) return
        if (hasLocationPermission) {
            onLocationGranted()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun onLocationGranted() {
        viewModel.fetchLocationAndWeather()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                onLocationGranted()
            } else {
                Log.e("Location", "Permission denied")
                viewModel.locationError.value = ErrorType.NoPermission
            }
        }

    private fun scheduleRunner() {
        val hr = PERIODIC_REFRESH_INTERVAL
        val cur = System.currentTimeMillis()
        val updateTarget = min(cur + hr, viewModel.lastUpdatedAt + hr)
        val delay = updateTarget - cur
        window?.decorView?.postDelayed(this, delay)
    }

    private fun cancelRunner() {
        window?.decorView?.removeCallbacks(this)
    }

    override fun run() {
        viewModel.refresh()
        scheduleRunner()
    }

    private fun loadAll() {
        checkAndRequestLocationPermission()
        viewModel.fetchWallpaper()
    }

    private inner class TickReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context?,
            intent: Intent?,
        ) {
            viewModel.time.value = System.currentTimeMillis()
        }
    }

    override val activityWindow: Window?
        get() = window

    override val insetState: MutableState<Boolean>
        get() = viewModel.insetVisible

    override val viewScope: CoroutineScope
        get() = lifecycleScope

}

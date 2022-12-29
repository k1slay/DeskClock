package com.k2.deskclock

import android.app.Application
import com.k2.deskclock.utils.DebugUtils
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DeskClockApp : Application() {

    @Inject
    lateinit var debugUtils: DebugUtils
    override fun onCreate() {
        super.onCreate()
        debugUtils.initFlipper()
    }

}

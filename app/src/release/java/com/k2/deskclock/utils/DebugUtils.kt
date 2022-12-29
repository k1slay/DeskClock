package com.k2.deskclock.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import javax.inject.Inject

class DebugUtils @Inject internal constructor(
    @ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient.Builder
) {

    fun initFlipper() {
        /* no-op */
    }

}

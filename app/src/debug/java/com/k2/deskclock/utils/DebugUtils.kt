package com.k2.deskclock.utils

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.core.FlipperClient
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin.SharedPreferencesDescriptor
import com.facebook.soloader.SoLoader
import com.k2.deskclock.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import java.io.File
import java.io.FileFilter
import javax.inject.Inject

class DebugUtils
    @Inject
    internal constructor(
        @ApplicationContext private val context: Context,
        private val okHttpClient: OkHttpClient.Builder,
    ) {
        fun initFlipper() {
            SoLoader.init(context, false)
            if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(context)) {
                val client: FlipperClient = AndroidFlipperClient.getInstance(context)
                client.addPlugin(InspectorFlipperPlugin(context, DescriptorMapping.withDefaults()))
                client.addPlugin(DatabasesFlipperPlugin(context))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    addSharedPrefPlugin(client)
                }
                makeNetworkPlugin(client)
                client.start()
            }
        }

        private fun makeNetworkPlugin(client: FlipperClient) {
            val networkFlipperPlugin = NetworkFlipperPlugin()
            okHttpClient.addInterceptor(FlipperOkhttpInterceptor(networkFlipperPlugin))
            client.addPlugin(networkFlipperPlugin)
        }

        @RequiresApi(Build.VERSION_CODES.N)
        private fun addSharedPrefPlugin(client: FlipperClient) {
            val dir = context.dataDir
            val spDir = File(dir, "shared_prefs")
            val list = mutableListOf<SharedPreferencesDescriptor>()
            spDir.listFiles(
                FileFilter {
                    it.extension == "xml"
                },
            )?.let { files ->
                for (fl in files) {
                    list.add(SharedPreferencesDescriptor(fl.name, Context.MODE_PRIVATE))
                }
            }
            val plugin = SharedPreferencesFlipperPlugin(context, list)
            client.addPlugin(plugin)
        }

    }

package com.voltfitness.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class VoltApp : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun newImageLoader(): ImageLoader = imageLoader
}

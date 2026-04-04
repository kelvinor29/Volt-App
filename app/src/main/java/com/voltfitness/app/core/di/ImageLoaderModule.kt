package com.voltfitness.app.core.di

import android.content.Context
import android.os.Build
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides a singleton [ImageLoader] configured with GIF support
 * and aggressive caching to minimize network calls for exercise GIFs.
 *
 * Disk cache persists GIFs across app sessions, so each exercise GIF
 * is downloaded only once from the ExerciseDB /image endpoint.
 */
@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {

    @Provides
    @Singleton
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader =
        ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.15) // 15% of app memory
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("exercise_gifs"))
                    .maxSizeBytes(100L * 1024 * 1024) // 100 MB max
                    .build()
            }
            .respectCacheHeaders(false) // Always cache, ignore server headers
            .build()
}

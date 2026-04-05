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

private const val CACHE_DIRECTORY = "exercise_gifs"
private const val DISK_CACHE_SIZE = 100L * 1024 * 1024 // 100 MB
private const val MEMORY_CACHE_PERCENT = 0.15

/**
 * Hilt module for configuring the application-wide [ImageLoader] via Coil.
 *
 * Optimized specifically for exercise demonstration GIFs, implementing aggressive
 * disk persistence to minimize API consumption and ensure offline availability
 * of training media.
 */
@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {

    /**
     * Provides a singleton [ImageLoader] with multi-level caching and GIF support.
     *
     * Configuration details:
     * - **Components**: Switches between [ImageDecoderDecoder] (API 28+) and [GifDecoder]
     *   for optimal hardware acceleration of GIF assets.
     * - **Memory Cache**: Allocates 15% of the available app memory to balance performance
     *   and system stability.
     * - **Disk Cache**: Persists up to 100MB of media in a dedicated "exercise_gifs" directory.
     * - **Cache Policy**: Explicitly ignores server headers to force long-term local storage.
     *
     * @param context Application context for directory access and resource management.
     */
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
                    .maxSizePercent(MEMORY_CACHE_PERCENT)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve(CACHE_DIRECTORY))
                    .maxSizeBytes(DISK_CACHE_SIZE)
                    .build()
            }
            .respectCacheHeaders(false)
            .build()
}
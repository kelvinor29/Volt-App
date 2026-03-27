package com.voltfitness.app.ui.screens.exercise.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.voltfitness.app.BuildConfig

/**
 * Reusable composable that displays an animated GIF for an exercise
 * from the ExerciseDB /image endpoint.
 *
 * Uses [SubcomposeAsyncImage] to show a loading indicator while the GIF
 * downloads, and a fallback icon if loading fails.
 *
 * The GIF is cached on disk by Coil after the first load, so subsequent
 * displays are instant with zero network calls.
 *
 * @param exerciseId The ExerciseDB exercise ID (e.g., "0001").
 * @param contentDescription Accessibility description.
 * @param modifier Modifier for the image container.
 * @param size Width and height of the image box.
 * @param resolution GIF resolution: 180, 360, 720, or 1080 pixels.
 */
@Composable
fun ExerciseGifImage(
    exerciseId: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 60.dp,
    resolution: Int = 360
) {
    val gifUrl = buildExerciseGifUrl(exerciseId, resolution)

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(gifUrl)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentScale = ContentScale.Crop,
        loading = {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(size / 3),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        error = {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Outlined.FitnessCenter,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(size / 2)
                )
            }
        }
    )
}

/**
 * Builds the ExerciseDB image streaming URL for a given exercise.
 *
 * This endpoint streams the GIF directly (authenticated via query param)
 * and does NOT expire like the gifUrl field in the exercise JSON.
 *
 * @see <a href="https://edb-docs.up.railway.app/docs/image-service/image">ExerciseDB Image Docs</a>
 */
fun buildExerciseGifUrl(exerciseId: String, resolution: Int = 360): String =
    "https://exercisedb.p.rapidapi.com/image" +
            "?exerciseId=$exerciseId" +
            "&resolution=$resolution" +
            "&rapidapi-key=${BuildConfig.EXERCISE_DB_API_KEY}"

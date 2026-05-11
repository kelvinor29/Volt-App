package com.voltfitness.app.ui.common

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource

/**
 * A sealed class used to handle text representation in the ViewModel and UI layers.
 * It abstracts the source of the string, allowing for both hardcoded strings
 * and Android String Resources with support for arguments.
 *
 * This pattern ensures that ViewModels remain agnostic of the Android Context
 * while still allowing for localized strings.
 */
sealed interface UiText {

    /**
     * Represents a raw [String]. Use this for dynamic text that doesn't
     * come from resources (e.g., API responses, user input).
     */
    data class DynamicString(val value: String) : UiText

    /**
     * Represents an Android String Resource.
     *
     * @property resId The R.string ID.
     * @property args Optional arguments for string formatting.
     */
    class StringResource(
        @StringRes val resId: Int,
        val args: List<Any> = emptyList()
    ) : UiText {
        // Custom equals and hashCode are required because List/Array comparison
        // behavior affects Compose's recomposition and State equality.
        constructor(@StringRes resId: Int, vararg args: Any) : this(resId, args.toList())

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as StringResource
            return resId == other.resId && args == other.args
        }

        override fun hashCode(): Int {
            var result = resId
            result = 31 * result + args.hashCode()
            return result
        }
    }

    /**
     * Represents an empty string state.
     */
    data object Empty : UiText

    /**
     * Resolves the [UiText] into a [String] within a Composable function.
     * Uses [ReadOnlyComposable] to optimize performance as it doesn't write to the composer.
     */
    @Composable
    @ReadOnlyComposable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args.toTypedArray())
            is Empty -> ""
        }
    }

    /**
     * Resolves the [UiText] into a [String] using a standard [Context].
     * Use this in non-composable scopes like Services, Adapters, or Notifications.
     */
    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args.toTypedArray())
            is Empty -> ""
        }
    }
}
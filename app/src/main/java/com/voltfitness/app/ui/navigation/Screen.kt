package com.voltfitness.app.ui.navigation

/**
 * Volt Navigation Routes
 *
 * Defines a type-safe navigation system for the application.
 * Each object represents a unique destination within the [VoltNavHost].
 */
sealed class Screen(val route: String) {

    // ========== AUTHENTICATION FLOW ==========

    /** Initial guard screen for session checking. */
    data object Splash : Screen("splash")

    /** Onboarding and user profile registration. */
    data object Register : Screen("register")

    // ========== MAIN DASHBOARD (BOTTOM NAV) ==========

    /** Primary dashboard with user weight and active routines. */
    data object Home : Screen("home")

    /**
     * Active training session.
     * @param routineTitle Friendly name of the routine to be displayed.
     */
    data object Train : Screen("train/{routineTitle}") {
        fun createRoute(routineTitle: String) = "train/$routineTitle"
    }

    /** Historical workout data and analytics. */
    data object History : Screen("history")

    /** App preferences and user profile management. */
    data object Settings : Screen("settings")

    // ========== BODY COMPOSITION FEATURE ==========

    /** List of historical body measurements. */
    data object BodyComposition : Screen("body_composition")

    /** Form to record new body metrics. */
    data object AddBodyComposition : Screen("body_composition/add")

    // ========== WORKOUT & ROUTINE MANAGEMENT ==========

    /**
     * Summary of a finished workout session.
     * @param workoutId Database ID for the completed workout.
     */
    data object WorkoutDetail : Screen("workout_detail/{workoutId}") {
        fun createRoute(workoutId: Long) = "workout_detail/$workoutId"
    }

    /**
     * Editor for routine structure and daily organization.
     * Supports both new (null) and existing IDs.
     */
    data object RoutineEditor : Screen("routine_editor?routineId={routineId}&folderId={folderId}") {
        fun createRoute(routineId: Long? = null, folderId: Long? = null): String {
            return "routine_editor?routineId=${routineId ?: -1L}&folderId=${folderId ?: -1L}"
        }
    }

    /**
     * Contextual exercise selector for routine construction.
     * @param routineId ID of the parent routine.
     * @param dayOrder Index of the routine day.
     */
    data object ExercisePicker : Screen("exercise_picker/{routineId}/{dayOrder}") {
        fun createRoute(routineId: Long, dayOrder: Int) = "exercise_picker/$routineId/$dayOrder"
    }

    /**
     * Technical details and GIF demonstration of an exercise.
     */
    data object ExerciseDetail : Screen("exercise_detail/{exerciseName}") {
        fun createRoute(exerciseName: String) = "exercise_detail/$exerciseName"
    }

    // ========== TODO: FUTURE NAVIGATION UTILITIES ==========

    companion object {
        /**
         * Defines the core destinations accessible via the [BottomNavigationBar].
         */
        fun getMainScreens() = listOf(
            Home,
            Train,
            History,
            Settings
        )
    }
}
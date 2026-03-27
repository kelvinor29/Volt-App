package com.voltfitness.app.ui.navigation

/**
 * VOLT NAVIGATION ROUTES
 *
 * A type-safe navigation system for the application.
 * Each object represents a unique destination in the NavGraph.
 */
sealed class Screen(val route: String) {

    // ========== AUTH DESTINATIONS ==========
    data object Splash : Screen("splash")
    data object Register : Screen("register")

    // ========== MAIN DESTINATIONS ==========

    /**
     * Dashboard screen showing user weight, daily routines, and progress.
     */
    data object Home : Screen("home")

    /**
     * Training session screen.
     * @param routineTitle The name of the routine to be performed.
     */
    data object Train : Screen("train/{routineTitle}") {
        fun createRoute(routineTitle: String) = "train/$routineTitle"
    }

    /**
     * Workout history, personal records, and statistics.
     */
    data object History : Screen("history")

    /**
     * User profile, preferences, and data export settings.
     */
    data object Settings : Screen("settings")

    // ========== BODY COMPOSITION DESTINATIONS ==========

    data object BodyComposition : Screen("body_composition")
    data object AddBodyComposition : Screen("body_composition/add")

    /**
     * Specific details of a completed workout.
     * @param workoutId Unique identifier for the workout.
     */
    data object WorkoutDetail : Screen("workout_detail/{workoutId}") {
        fun createRoute(workoutId: Long) = "workout_detail/$workoutId"
    }

    data object RoutineDetail : Screen("routine_detail/{routineId}") {
        fun createRoute(routineId: Long) = "routine_detail/$routineId"
    }

    data object RoutineCreator : Screen("routine_creator/{folderId}") {
        fun createRoute(folderId: Long): String = "routine_creator/$folderId"
    }

    data object ExercisePicker : Screen("exercise_picker/{routineId}/{dayOrder}") {
        fun createRoute(routineId: Long, dayOrder: Int) = "exercise_picker/$routineId/$dayOrder"
    }

    /**
     * Detailed information about a specific exercise technique.
     * @param exerciseName Name of the exercise.
     */
    data object ExerciseDetail : Screen("exercise_detail/{exerciseName}") {
        fun createRoute(exerciseName: String) = "exercise_detail/$exerciseName"
    }

    // ========== HELPER UTILS ==========

    companion object {
        /**
         * Returns the list of screens that should appear in the Bottom Navigation Bar.
         */
        fun getMainScreens() = listOf(
            Home,
            Train,
            History,
            Settings
        )
    }

}
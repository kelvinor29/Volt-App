package com.voltfitness.app.ui.screens.register

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material.icons.outlined.Transgender
import androidx.compose.material.icons.outlined.Wc
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.voltfitness.app.core.designsystem.component.VoltButton
import com.voltfitness.app.core.designsystem.component.VoltDropdownSelector
import com.voltfitness.app.core.designsystem.component.VoltErrorBanner
import com.voltfitness.app.core.designsystem.component.VoltSecondaryButton
import com.voltfitness.app.core.designsystem.component.VoltTextField
import com.voltfitness.app.core.common.ProfileOptions
import com.voltfitness.app.ui.theme.VoltTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.Preview
import com.voltfitness.app.core.designsystem.component.VoltDateSelector
import com.voltfitness.app.ui.screens.body_composition.add.AddBodyCompositionEvent

@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    onEvent: (RegisterEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Header
            Text(
                text = "Welcome to Volt",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Set up your profile to get started",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Progress indicator
            LinearProgressIndicator(
                progress = { (uiState.currentStep + 1) / 3f },
                modifier = Modifier.fillMaxWidth(),
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Error banner
            uiState.errorMessage?.let { error ->
                VoltErrorBanner(
                    message = error,
                    onDismiss = { onEvent(RegisterEvent.DismissError) }
                )
            }

            // Animated step content
            AnimatedContent(
                targetState = uiState.currentStep,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally { it } + fadeIn())
                            .togetherWith(slideOutHorizontally { -it } + fadeOut())
                    } else {
                        (slideInHorizontally { -it } + fadeIn())
                            .togetherWith(slideOutHorizontally { it } + fadeOut())
                    }
                },
                label = "register_step"
            ) { step ->
                when (step) {
                    0 -> StepBasicInfo(uiState, onEvent)
                    1 -> StepFitnessProfile(uiState, onEvent)
                    2 -> StepReview(uiState)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (uiState.currentStep > 0) {
                    VoltSecondaryButton(
                        text = "Back",
                        onClick = { onEvent(RegisterEvent.PreviousStep) },
                        modifier = Modifier.weight(1f)
                    )
                }
                VoltButton(
                    text = if (uiState.currentStep == 2) "Create Profile" else "Next",
                    onClick = {
                        if (uiState.currentStep == 2) onEvent(RegisterEvent.Submit)
                        else onEvent(RegisterEvent.NextStep)
                    },
                    enabled = when (uiState.currentStep) {
                        0 -> uiState.isNameValid && uiState.isGenderValid
                        1 -> uiState.isActivityValid && uiState.isGoalValid && uiState.isExperienceLvlValid
                        else -> true
                    },
                    isLoading = uiState.isLoading,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun StepBasicInfo(
    uiState: RegisterUiState,
    onEvent: (RegisterEvent) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Basic Information",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        VoltTextField(
            value = uiState.name,
            onValueChange = { onEvent(RegisterEvent.UpdateName(it)) },
            label = "Name *",
            leadingIcon = Icons.Outlined.Person,
            isError = uiState.name.isNotBlank() && !uiState.isNameValid,
            errorMessage = "Name is required",
            imeAction = ImeAction.Next
        )

        VoltTextField(
            value = uiState.email,
            onValueChange = { onEvent(RegisterEvent.UpdateEmail(it)) },
            label = "Email",
            leadingIcon = Icons.Outlined.Email,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )

        VoltDropdownSelector(
            label = "Gender *",
            options = ProfileOptions.genders,
            selectedOption = uiState.gender,
            leadingIcon = Icons.Outlined.Wc,
            onOptionSelected = { onEvent(RegisterEvent.UpdateGender(it)) },
            isError = uiState.gender.isNotBlank() && !uiState.isGenderValid,
        )

        VoltDateSelector(
            label = "Birth Date",
            selectedDate = uiState.birthDate,
            onDateSelected = { onEvent(RegisterEvent.UpdateBirthDate(it)) },
            icon = Icons.Outlined.CalendarMonth,
        )
    }
}

@Composable
private fun StepFitnessProfile(
    uiState: RegisterUiState,
    onEvent: (RegisterEvent) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Fitness Profile",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        VoltDropdownSelector(
            label = "Activity Level",
            options = ProfileOptions.activityLevels,
            selectedOption = uiState.activityLevel,
            leadingIcon = Icons.Outlined.Speed,
            onOptionSelected = { onEvent(RegisterEvent.UpdateActivityLevel(it)) },
        )

        VoltDropdownSelector(
            label = "Goal",
            options = ProfileOptions.goals,
            selectedOption = uiState.goal,
            leadingIcon = Icons.Outlined.TrackChanges,
            onOptionSelected = { onEvent(RegisterEvent.UpdateGoal(it)) }
        )

        VoltDropdownSelector(
            label = "Experience Level",
            options = ProfileOptions.experienceLevels,
            selectedOption = uiState.experienceLevel,
            leadingIcon = Icons.Outlined.Psychology,
            onOptionSelected = { onEvent(RegisterEvent.UpdateExperienceLevel(it)) }
        )

        VoltTextField(
            value = uiState.gymName,
            onValueChange = { onEvent(RegisterEvent.UpdateGymName(it)) },
            label = "Gym Name",
            leadingIcon = Icons.Outlined.Place,
            imeAction = ImeAction.Done
        )
    }
}

@Composable
private fun StepReview(
    uiState: RegisterUiState,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Review Your Profile",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Confirm your details before creating your profile.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        ReviewItem(label = "Name", value = uiState.name)
        ReviewItem(label = "Email", value = uiState.email.ifBlank { "Not set" })
        ReviewItem(label = "Gender", value = uiState.gender.ifBlank { "Not set" })
        ReviewItem(label = "Activity Level", value = uiState.activityLevel.ifBlank { "Not set" })
        ReviewItem(label = "Goal", value = uiState.goal.ifBlank { "Not set" })
        ReviewItem(label = "Experience", value = uiState.experienceLevel.ifBlank { "Not set" })
        ReviewItem(label = "Gym", value = uiState.gymName.ifBlank { "Not set" })
    }
}

@Composable
private fun ReviewItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(name = "Step 1: Basic Info", showBackground = true)
@Composable
private fun RegisterStep1Preview() {
    VoltTheme {
        Surface {
            RegisterScreen(
                uiState = RegisterUiState(
                    currentStep = 0,
                    name = "John Doe"
                ),
                onEvent = {}
            )
        }
    }
}

@Preview(name = "Step 2: Fitness Profile", showBackground = true)
@Composable
private fun RegisterStep2Preview() {
    VoltTheme {
        Surface {
            RegisterScreen(
                uiState = RegisterUiState(
                    currentStep = 1,
                    activityLevel = "Moderate",
                    goal = "Build Muscle"
                ),
                onEvent = {}
            )
        }
    }
}

@Preview(name = "Step 3: Review", showBackground = true)
@Composable
private fun RegisterStep3Preview() {
    VoltTheme {
        Surface {
            RegisterScreen(
                uiState = RegisterUiState(
                    currentStep = 2,
                    name = "John Doe",
                    email = "john@volt.com",
                    gender = "Male",
                    activityLevel = "High",
                    goal = "Weight Loss",
                    experienceLevel = "Intermediate",
                    gymName = "Volt HQ"
                ),
                onEvent = {}
            )
        }
    }
}

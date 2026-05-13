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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.voltfitness.app.R
import com.voltfitness.app.core.designsystem.component.VoltDateSelector

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
                text = stringResource(R.string.welcome_to_volt),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = stringResource(R.string.set_up_your_profile_to_get_started),
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
                    text = if (uiState.currentStep == 2) stringResource(R.string.create_profile) else "Next",
                    onClick = {
                        if (uiState.currentStep == 2) onEvent(RegisterEvent.Submit)
                        else onEvent(RegisterEvent.NextStep)
                    },
                    enabled = when (uiState.currentStep) {
                        0 -> uiState.isNameValid && uiState.isGenderValid && uiState.isBirthDateValid
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
            text = stringResource(R.string.basic_information),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        VoltTextField(
            value = uiState.name,
            onValueChange = { onEvent(RegisterEvent.UpdateName(it)) },
            label = stringResource(R.string.name),
            leadingIcon = Icons.Outlined.Person,
            isError = uiState.name.isNotBlank() && !uiState.isNameValid,
            errorMessage = stringResource(R.string.name_is_required),
            imeAction = ImeAction.Next
        )

        VoltTextField(
            value = uiState.email,
            onValueChange = { onEvent(RegisterEvent.UpdateEmail(it)) },
            label = stringResource(R.string.email),
            leadingIcon = Icons.Outlined.Email,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )

        VoltDropdownSelector(
            label = stringResource(R.string.gender),
            options = ProfileOptions.Gender.entries.map { gender -> stringResource(gender.labelRes) },
            selectedOption = uiState.gender,
            leadingIcon = Icons.Outlined.Wc,
            onOptionSelected = { onEvent(RegisterEvent.UpdateGender(it)) },
            isError = uiState.gender.isNotBlank() && !uiState.isGenderValid,
        )

        VoltDateSelector(
            label = stringResource(R.string.birth_date),
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
            text = stringResource(R.string.fitness_profile),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        VoltDropdownSelector(
            label = stringResource(R.string.activity_level),
            options = ProfileOptions.ActivityLevel.entries.map { gender -> stringResource(gender.labelRes) },
            selectedOption = uiState.activityLevel,
            leadingIcon = Icons.Outlined.Speed,
            onOptionSelected = { onEvent(RegisterEvent.UpdateActivityLevel(it)) },
        )

        VoltDropdownSelector(
            label = stringResource(R.string.goal),
            options = ProfileOptions.FitnessGoal.entries.map { gender -> stringResource(gender.labelRes) },
            selectedOption = uiState.goal,
            leadingIcon = Icons.Outlined.TrackChanges,
            onOptionSelected = { onEvent(RegisterEvent.UpdateGoal(it)) }
        )

        VoltDropdownSelector(
            label = stringResource(R.string.experience_level),
            options = ProfileOptions.ExperienceLevel.entries.map { gender -> stringResource(gender.labelRes) },
            selectedOption = uiState.experienceLevel,
            leadingIcon = Icons.Outlined.Psychology,
            onOptionSelected = { onEvent(RegisterEvent.UpdateExperienceLevel(it)) }
        )

        VoltTextField(
            value = uiState.gymName,
            onValueChange = { onEvent(RegisterEvent.UpdateGymName(it)) },
            label = stringResource(R.string.gym_name),
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
            text = stringResource(R.string.review_your_profile),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = stringResource(R.string.confirm_your_details_before_creating_your_profile),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        ReviewItem(label = stringResource(R.string.name), value = uiState.name)
        ReviewItem(label = stringResource(R.string.email), value = uiState.email.ifBlank {
            stringResource(
                R.string.not_set
            )
        })
        ReviewItem(label = stringResource(R.string.gender), value = uiState.gender.ifBlank {
            stringResource(
                R.string.not_set
            )
        })
        ReviewItem(
            label = stringResource(R.string.activity_level),
            value = uiState.activityLevel.ifBlank { stringResource(R.string.not_set) })
        ReviewItem(label = stringResource(R.string.goal), value = uiState.goal.ifBlank {
            stringResource(
                R.string.not_set
            )
        })
        ReviewItem(
            label = stringResource(R.string.experience),
            value = uiState.experienceLevel.ifBlank {
                stringResource(
                    R.string.not_set
                )
            })
        ReviewItem(label = stringResource(R.string.gym), value = uiState.gymName.ifBlank {
            stringResource(
                R.string.not_set
            )
        })
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

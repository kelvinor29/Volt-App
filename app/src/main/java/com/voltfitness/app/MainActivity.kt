package com.voltfitness.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.voltfitness.app.ui.components.VoltTopAppBar
import com.voltfitness.app.ui.navigation.TopAppBarState
import com.voltfitness.app.ui.navigation.VoltNavGraph
import com.voltfitness.app.ui.theme.VoltTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.voltfitness.app.ui.navigation.Screen
import com.voltfitness.app.ui.session.SessionState
import com.voltfitness.app.ui.session.SessionViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VoltTheme {
                VoltRoot()
            }
        }
    }
}

@Composable
fun VoltRoot() {
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val sessionState by sessionViewModel.sessionState.collectAsStateWithLifecycle()

    when (sessionState) {
        is SessionState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        is SessionState.Authenticated,
        is SessionState.Unauthenticated -> {
            val startDestination = when (sessionState) {
                is SessionState.Authenticated -> Screen.Home.route
                else -> Screen.Register.route
            }

            val navController = rememberNavController()
            val topAppBarState = remember { mutableStateOf(TopAppBarState()) }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    VoltTopAppBar(state = topAppBarState.value)
                }
            ) { innerPadding ->
                VoltNavGraph(
                    navController = navController,
                    topAppBarState = topAppBarState,
                    sessionViewModel = sessionViewModel,
                    modifier = Modifier.padding(innerPadding),
                    startDestination = startDestination
                )
            }

        }
    }
}


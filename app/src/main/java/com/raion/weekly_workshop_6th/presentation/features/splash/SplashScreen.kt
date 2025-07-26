package com.raion.weekly_workshop_6th.presentation.features.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navigateToNotes: () -> Unit,
    navigateToLogin: () -> Unit,
    viewModel: SplashViewModel = koinViewModel(),
) {
    val hasToken by viewModel.hasToken.collectAsState()

    LaunchedEffect(hasToken) {
        when (hasToken) {
            true -> navigateToNotes()
            false -> navigateToLogin()
            null -> {} // do nothing while loading
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Splash...", style = MaterialTheme.typography.headlineMedium)
    }
}
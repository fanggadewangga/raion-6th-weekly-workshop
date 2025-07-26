package com.raion.weekly_workshop_6th.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.raion.weekly_workshop_6th.presentation.features.auth.login.LoginScreen
import com.raion.weekly_workshop_6th.presentation.features.auth.register.RegisterScreen
import com.raion.weekly_workshop_6th.presentation.features.note.detail.NoteDetailScreen
import com.raion.weekly_workshop_6th.presentation.features.note.notes.NotesScreen
import com.raion.weekly_workshop_6th.presentation.features.splash.SplashScreen
import com.raion.weekly_workshop_6th.presentation.navigation.ScreenRoute
import com.raion.weekly_workshop_6th.presentation.ui.theme.Weeklyworkshop6thTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Weeklyworkshop6thTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = ScreenRoute.Splash) {
                        composable<ScreenRoute.Splash> {
                            SplashScreen(
                                navigateToLogin = { navController.navigate(ScreenRoute.Login) { popUpTo<ScreenRoute.Splash> { inclusive = true } } },
                                navigateToNotes = { navController.navigate(ScreenRoute.Notes) { popUpTo<ScreenRoute.Splash> { inclusive = true } } },
                            )
                        }

                        // auth related
                        composable<ScreenRoute.Register> {
                            RegisterScreen(
                                navigateToLogin = { navController.navigate(ScreenRoute.Login) { popUpTo<ScreenRoute.Register> { inclusive = true } } }
                            )
                        }

                        composable<ScreenRoute.Login> {
                            LoginScreen(
                                navigateToRegister = { navController.navigate(ScreenRoute.Register) { popUpTo<ScreenRoute.Login> { inclusive = true }  } },
                                navigateToNotes = { navController.navigate(ScreenRoute.Notes) { popUpTo<ScreenRoute.Login> { inclusive = true }  } }
                            )
                        }

                        // note related
                        composable<ScreenRoute.Notes> {
                            NotesScreen(onNoteClick = { noteId -> navController.navigate(ScreenRoute.NoteDetail(noteId)) })
                        }

                        composable<ScreenRoute.NoteDetail> {
                            val noteId = it.toRoute<ScreenRoute.NoteDetail>().id
                            NoteDetailScreen(
                                noteId = noteId,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
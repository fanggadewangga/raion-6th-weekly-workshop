package com.raion.weekly_workshop_6th.di

import com.raion.weekly_workshop_6th.presentation.features.auth.login.LoginViewModel
import com.raion.weekly_workshop_6th.presentation.features.auth.register.RegisterViewModel
import com.raion.weekly_workshop_6th.presentation.features.note.detail.NoteDetailViewModel
import com.raion.weekly_workshop_6th.presentation.features.note.notes.NotesViewModel
import com.raion.weekly_workshop_6th.presentation.features.splash.SplashViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // token storage

    // ktor client

    // provide repositories
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<NoteRepository> { NoteRepositoryImpl(get()) }

    // provide viewmodels
    viewModel { SplashViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { NotesViewModel(get()) }
    viewModel { NoteDetailViewModel(get()) }
}
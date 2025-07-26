package com.raion.weekly_workshop_6th.di

import com.raion.weekly_workshop_6th.data.local.TokenStorage
import com.raion.weekly_workshop_6th.data.remote.AuthInterceptor
import com.raion.weekly_workshop_6th.data.repository.AuthRepositoryImpl
import com.raion.weekly_workshop_6th.data.repository.NoteRepositoryImpl
import com.raion.weekly_workshop_6th.domain.repository.AuthRepository
import com.raion.weekly_workshop_6th.domain.repository.NoteRepository
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
    single { TokenStorage(get()) }

    single {
        val tokenStorage: TokenStorage = get()


        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                })
            }

            // Log both request and response bodies
            install(Logging) { level = LogLevel.BODY }

            // Install your custom interceptor for Authorization header + optional refresh logic
            install(AuthInterceptor(tokenStorage))

            expectSuccess = false

            // Default configuration for all requests
            defaultRequest {

                // Add only static jwt configuration here
//                val token = tokenStorage.getToken()
//                if (!token.isNullOrBlank()) {
//                    header("Authorization", token)
//                }

                // base url
                // default : https://notes.elginbrian.com/api/
                url {
                    protocol = URLProtocol.HTTPS
                    host = "notes.elginbrian.com"
                }
            }
        }
    }


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
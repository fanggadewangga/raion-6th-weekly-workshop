package com.raion.weekly_workshop_6th.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface ScreenRoute {
    @Serializable
    data object Splash : ScreenRoute

    @Serializable
    data object Login : ScreenRoute

    @Serializable
    data object Register : ScreenRoute

    @Serializable
    data object Notes : ScreenRoute

    @Serializable
    data class NoteDetail(val id: String?) : ScreenRoute
}
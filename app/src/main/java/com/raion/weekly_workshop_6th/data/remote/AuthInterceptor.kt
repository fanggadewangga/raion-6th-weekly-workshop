package com.raion.weekly_workshop_6th.data.remote

import com.raion.weekly_workshop_6th.data.local.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.util.AttributeKey

class AuthInterceptor(private val tokenStorage: TokenStorage) : HttpClientPlugin<Unit, AuthInterceptor> {
    override val key: AttributeKey<AuthInterceptor> = AttributeKey("AuthInterceptor")

    override fun install(
        plugin: AuthInterceptor,
        scope: HttpClient
    ) {
        scope.requestPipeline.intercept(HttpRequestPipeline.State) {
            val token = tokenStorage.getToken()
            if (!token.isNullOrBlank()) {
                context.headers.append("Authorization", token)
            }
            proceed()
        }
    }

    override fun prepare(block: Unit.() -> Unit) = this
}

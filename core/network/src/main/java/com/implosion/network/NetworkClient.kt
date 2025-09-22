package com.implosion.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object NetworkClient {
    val httpClient = HttpClient(Android) {

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(Logging) {
            level = LogLevel.ALL
            logger = io.ktor.client.plugins.logging.Logger.ANDROID
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 300_000  // 5 минут для streaming
            connectTimeoutMillis = 15_000
            socketTimeoutMillis = 300_000   // 5 минут для streaming
        }

        defaultRequest {
            url("http://195.225.111.210:8080/") //:5000 - порт для транскрибации
            headers.append("Accept", "application/json")
            headers.append("Content-Type", "application/json")
        }
    }
}
package com.exampro.app.di

import com.exampro.app.data.api.*
import com.exampro.app.data.repository.AuthRepository
import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

val appModule = module {
    // 1. The HttpClient (Singleton)
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
        }
    }

    // 2. Multiplatform Settings (Singleton)
    single { Settings() }

    // 3. API Services
    // get() automatically injects the HttpClient defined above
    single { AuthApi(get()) }
    single { DeviceApi(get()) }
    single { ExamApi(get()) }
    single { SubjectApi(get()) }
    single { QuestionApi(get()) }
    single { DashboardApi(get()) }

    // 4. The AuthRepository Singleton
    // get() automatically injects AuthApi, DeviceApi, and Settings
    single { AuthRepository(get(), get(), get()) }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(appModule)
}
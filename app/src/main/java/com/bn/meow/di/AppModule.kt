package com.bn.meow.di

import android.util.Log
import com.bn.meow.ui.screens.MainViewModel
import com.bn.meow.MainActivity
import com.bn.meow.domain.repository.MainRepository
import com.bn.meow.data.repository.MainRepositoryImp
import com.bn.meow.domain.usecase.GetCatsUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import org.koin.dsl.module

import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named



val appModule = module {
    single { createKtorClient() }
    single<MainRepository> { MainRepositoryImp(get()) }
    single { GetCatsUseCase(get()) }
    viewModel{
        MainViewModel(get())
    }
}
val activityModule = module {
    scope<MainActivity> {
        scoped(qualifier = named("hello")) { "Heloo" }
        scoped(qualifier = named("bye")) { "Byeeeeee" }
    }
}



private const val TIME_OUT = 60_000


fun createKtorClient(): HttpClient {
    return HttpClient(Android) {
        // Configure your Ktor client here
        install(ContentNegotiation) {
            engine {
                connectTimeout = TIME_OUT
                socketTimeout = TIME_OUT
            }
            json(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            })

        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("HTTP:Logger=>", message)
                }

            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse { response ->
                Log.d("HTTP:status:", "${response.status.value}")
            }
        }

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }
}



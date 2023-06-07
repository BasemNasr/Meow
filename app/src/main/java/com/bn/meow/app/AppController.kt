package com.bn.meow.app
import android.app.Application
import com.bn.meow.di.activityModule
import com.bn.meow.di.appModule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class AppController : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@AppController)
            modules(appModule, activityModule)
        }
    }

}
package com.mathi.finance

import android.app.Application
import com.mathi.finance.core.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyFinanceApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyFinanceApp)
            modules(appModule)
        }
    }
}

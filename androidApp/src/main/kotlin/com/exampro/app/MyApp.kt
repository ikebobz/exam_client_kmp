package com.exampro.app

import android.app.Application
import com.exampro.app.di.dataStoreModule
import com.exampro.app.di.initKoin
import org.koin.android.ext.koin.androidContext // This should now resolve



class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            additionalModules = listOf(
                dataStoreModule(this) // Pass the Context here
                // other android modules...
            )

        ){
            androidContext(this@MyApp)
        }

    }
}
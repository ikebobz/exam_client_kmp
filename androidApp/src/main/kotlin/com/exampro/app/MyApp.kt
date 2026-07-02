package com.exampro.app

import android.app.Application
import com.exampro.app.di.dataStoreModule
import com.exampro.app.di.initKoin
import com.exampro.app.utils.AndroidContext
import org.koin.android.ext.koin.androidContext // This should now resolve



class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidContext.instance = this
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
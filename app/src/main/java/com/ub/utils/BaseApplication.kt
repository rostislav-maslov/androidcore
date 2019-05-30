package com.ub.utils

import android.app.Application
import com.ub.utils.di.components.AppComponent
import com.ub.utils.di.components.DaggerAppComponent
import com.ub.utils.di.modules.ApiModule

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        UbUtils.init(this)

        initDI()
    }

    private fun initDI() {
        appComponent = DaggerAppComponent
            .builder()
            .apiModule(ApiModule())
            .build()
    }

    companion object {

        lateinit var appComponent: AppComponent
            private set
    }
}
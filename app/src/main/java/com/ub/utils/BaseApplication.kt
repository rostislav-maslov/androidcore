package com.ub.utils

import android.app.Application
import com.ub.utils.di.components.AppComponent
import com.ub.utils.di.components.DaggerAppComponent
import com.ub.utils.di.components.MainSubcomponent
import com.ub.utils.di.modules.ApiModule
import com.ub.utils.di.modules.MainModule

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
        private var mainSubcomponent: MainSubcomponent? = null

        fun getMainSubcomponent(): MainSubcomponent {
            if (mainSubcomponent == null) {
                mainSubcomponent = appComponent.mainSubcomponent(MainModule)
            }

            return mainSubcomponent ?: throw IllegalStateException("mainSubcomponent has been cleared after call")
        }

        fun clearMainSubcomponent() {
            mainSubcomponent = null
        }
    }
}
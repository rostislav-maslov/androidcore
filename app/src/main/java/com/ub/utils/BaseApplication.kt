package com.ub.utils

import android.app.Application

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        UbUtils.context = this
    }
}
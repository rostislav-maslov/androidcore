package com.ub.utils.di.components

import com.ub.utils.di.modules.MainModule
import com.ub.utils.di.modules.MainScope
import com.ub.utils.ui.main.MainPresenter
import dagger.Subcomponent

@MainScope
@Subcomponent(modules = [MainModule::class])
interface MainSubcomponent {
    fun inject(view: MainPresenter)
}
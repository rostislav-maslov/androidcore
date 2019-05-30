package com.ub.utils.di.components

import com.ub.utils.di.modules.ApiModule
import com.ub.utils.ui.main.presenters.MainPresenter
import com.ub.utils.ui.main.repositories.MainRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class])
interface AppComponent {

    fun inject(main : MainRepository)
}
package com.ub.utils.di.modules

import com.ub.utils.di.services.ApiService
import com.ub.utils.ui.main.IMainRepository
import com.ub.utils.ui.main.MainInteractor
import com.ub.utils.ui.main.MainRepository
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Module
object MainModule {

    @JvmStatic
    @MainScope
    @Provides
    fun provideInteractor(repository: IMainRepository): MainInteractor {
        return MainInteractor(repository)
    }

    @JvmStatic
    @MainScope
    @Provides
    fun provideRepository(apiService: ApiService): IMainRepository {
        return MainRepository(apiService)
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MainScope
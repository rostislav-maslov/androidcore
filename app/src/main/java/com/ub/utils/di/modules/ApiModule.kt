package com.ub.utils.di.modules

import com.ub.utils.di.services.ApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideApi() : ApiService {
        return ApiService()
    }
}
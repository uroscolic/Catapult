package com.rma.catapult.cat.list.api.di

import com.rma.catapult.cat.list.api.CatApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CatsModule {
    @Provides
    @Singleton
    fun provideCatApi(retrofit: Retrofit): CatApi = retrofit.create()
}
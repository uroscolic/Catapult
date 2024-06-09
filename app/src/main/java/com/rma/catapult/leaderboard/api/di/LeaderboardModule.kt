package com.rma.catapult.leaderboard.api.di

import com.rma.catapult.cat.list.api.CatApi
import com.rma.catapult.leaderboard.api.LeaderboardApi
import com.rma.catapult.networking.di.qualifiers.LeaderboardApiAnnotation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LeaderboardModule {
    @Provides
    @Singleton
    fun provideLeaderboardApi(@LeaderboardApiAnnotation retrofit: Retrofit): LeaderboardApi = retrofit.create()
}
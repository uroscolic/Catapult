package com.rma.catapult.user.auth.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.rma.catapult.user.auth.AuthDataSerializer
import com.rma.catapult.user.model.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthStoreModule {

    @Provides
    @Singleton
    fun provideAuthDataStore(
        @ApplicationContext context: Context,
    ): DataStore<User> =
        DataStoreFactory.create(
            produceFile = { context.dataStoreFile("auth.txt") },
            serializer = AuthDataSerializer(),
        )

}
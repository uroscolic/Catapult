package com.rma.catapult

import android.app.Application
import android.util.Log
import com.rma.catapult.user.auth.AuthStore
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CatapultApp : Application(){

    @Inject lateinit var authStore: AuthStore
    override fun onCreate() {
        super.onCreate()
        val authData = authStore.authData.value
        Log.d("authData", authData.toString())

    }
}
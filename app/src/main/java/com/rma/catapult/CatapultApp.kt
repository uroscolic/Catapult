package com.rma.catapult

import android.app.Application
import com.rma.catapult.db.CatapultDatabase

class CatapultApp : Application(){
    override fun onCreate() {
        super.onCreate()
        CatapultDatabase.initDatabase(this)
    }
}
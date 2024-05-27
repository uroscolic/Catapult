package com.rma.catapult

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rma.catapult.repository.Repository
import com.rma.catapult.core.theme.CatapultTheme
import com.rma.catapult.navigation.AppNavigation

class MainActivity : ComponentActivity() {

    val data = Repository.allData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatapultTheme {
                AppNavigation()
            }
        }
    }
}




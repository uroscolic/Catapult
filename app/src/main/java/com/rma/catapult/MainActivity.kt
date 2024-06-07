package com.rma.catapult

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rma.catapult.cat.repository.Repository
import com.rma.catapult.core.theme.CatapultTheme
import com.rma.catapult.core.theme.EnableEdgeToEdge
import com.rma.catapult.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatapultTheme {
                EnableEdgeToEdge()
                AppNavigation()
            }
        }
    }
}




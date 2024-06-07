package com.rma.catapult.drawer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    onDrawerDestinationClick: (AppDrawerDestination) -> Unit,
) {
    val uiScope = rememberCoroutineScope()
    val viewModel = hiltViewModel<AppDrawerViewModel>()


    BackHandler(enabled = drawerState.isOpen) {
        uiScope.launch { drawerState.close() }
    }

    val uiState = viewModel.state.collectAsState()

    AppDrawer(
        state = uiState.value,
        onDrawerDestinationClick = {
            uiScope.launch { drawerState.close() }
            onDrawerDestinationClick(it)
        },
        eventPublisher = {
            // TODO Publish event
        },
    )
}

@Composable
fun AppDrawer(
    state: AppDrawerContract.UiState,
    eventPublisher: (AppDrawerContract.UiEvent) -> Unit,
    onDrawerDestinationClick: (AppDrawerDestination) -> Unit,
) {
    Surface {
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .navigationBarsPadding()
                .width(300.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom,
        ) {

        }
    }
}
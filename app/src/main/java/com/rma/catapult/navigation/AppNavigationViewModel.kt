package com.rma.catapult.navigation

import androidx.lifecycle.ViewModel
import com.rma.catapult.leaderboard.LeaderboardState
import com.rma.catapult.user.auth.AuthStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor(
    private val authStore: AuthStore
) : ViewModel() {

    private val _state = MutableStateFlow(AppNavigationState())
    val state = _state.asStateFlow()

    private fun setState(reducer: AppNavigationState.() -> AppNavigationState) {
        _state.update(reducer)
    }

    init {
        checkIfUserIsRegistered()
    }

    private fun checkIfUserIsRegistered() {
        val authData = authStore.authData.value
        if (authData.name.isNotEmpty() && authData.surname.isNotEmpty() && authData.nickname.isNotEmpty() && authData.email.isNotEmpty()) {
            setState { copy(registered = true) }
        }
    }

}
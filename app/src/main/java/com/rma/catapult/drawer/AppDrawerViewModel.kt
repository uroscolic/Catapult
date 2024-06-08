package com.rma.catapult.drawer

import androidx.lifecycle.ViewModel
import com.rma.catapult.drawer.AppDrawerContract.*
import com.rma.catapult.user.auth.AuthStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AppDrawerViewModel @Inject constructor(
    authStore: AuthStore
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: UiState.() -> UiState) = _state.update(reducer)
}
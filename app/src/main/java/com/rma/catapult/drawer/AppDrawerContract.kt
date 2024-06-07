package com.rma.catapult.drawer

interface AppDrawerContract {

    data class UiState(
        val username: String = "",
    )
    sealed class UiEvent {
        data object LogoutConfirmed : UiEvent()
    }
}
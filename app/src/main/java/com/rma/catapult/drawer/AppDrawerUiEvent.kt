package com.rma.catapult.drawer



sealed class AppDrawerUiEvent {

    data class UserEdited(val nickname: String) : AppDrawerUiEvent()
}
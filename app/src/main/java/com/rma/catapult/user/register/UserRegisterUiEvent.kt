package com.rma.catapult.user.register

import com.rma.catapult.user.model.User

sealed class UserRegisterUiEvent {
    data class UserRegistered(val user: User) : UserRegisterUiEvent()

}
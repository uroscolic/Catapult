package com.rma.catapult.user.edit

import com.rma.catapult.user.model.User

sealed class UserEditUiEvent {

    data class UserEdited(val user: User) : UserEditUiEvent()
}
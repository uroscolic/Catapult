package com.rma.catapult.user.edit

interface UserEditContract {

    data class EditState(
        val name: String = "",
        val surname: String = "",
        val nickname: String = "",
        val email: String = ""
    )
}
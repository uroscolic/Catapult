package com.rma.catapult.user.register

interface UserRegisterContract {

    data class RegisterState(
        val registered: Boolean = false,
        val name: String = "",
        val surname: String = "",
        val nickname: String = "",
        val email: String = ""
    )

}
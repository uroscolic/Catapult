package com.rma.catapult.user.model

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val name: String,
    val email: String,
    val surname: String,
    val nickname: String
)
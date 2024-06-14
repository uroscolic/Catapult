package com.rma.catapult.user.profile

import com.rma.catapult.user.model.QuizResult
import com.rma.catapult.user.model.User

data class UserProfileState (
    val user: User = User("", "", "", "",
        emptyList(),QuizResult(0, 0, 0.0, 0)
        ,Int.MAX_VALUE)
)
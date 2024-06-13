package com.rma.catapult.user.model

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val name: String,
    val email: String,
    val surname: String,
    val nickname: String,
    val results: List<QuizResult> = emptyList(),
    val bestResult: QuizResult = QuizResult(0, 0, 0.0, 0),
    val bestRank: Int = Int.MAX_VALUE
)

@Serializable
data class QuizResult(
    val position: Int = 0,
    val category: Int = 0,
    val result: Double = 0.0,
    val createdAt: Long = 0
)
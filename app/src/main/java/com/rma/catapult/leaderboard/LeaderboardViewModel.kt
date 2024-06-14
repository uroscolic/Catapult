package com.rma.catapult.leaderboard

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.catapult.leaderboard.api.model.LeaderboardUiModel
import com.rma.catapult.leaderboard.model.Leaderboard
import com.rma.catapult.leaderboard.model.LeaderboardPost
import com.rma.catapult.leaderboard.model.LeaderboardResponse
import com.rma.catapult.leaderboard.model.Result
import com.rma.catapult.leaderboard.repository.LeaderboardRepository
import com.rma.catapult.user.auth.AuthStore
import com.rma.catapult.user.model.QuizResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository,
    private val authStore: AuthStore
): ViewModel() {

    private val _state = MutableStateFlow(LeaderboardState())
    val state = _state.asStateFlow()


    private val events = MutableSharedFlow<LeaderboardUiEvent>()
    private fun setState(reducer: LeaderboardState.() -> LeaderboardState) {
        _state.update(reducer)
    }
    fun setEvent(event: LeaderboardUiEvent){
        viewModelScope.launch {
            events.emit(event)
        }
    }
    init {
        fetchResults()
        observeEvents()
    }

    private fun fetchResults() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {

                val results = repository.fetchAllResults()
                setState {
                    copy(
                        loading = false,
                        results = results.map { it.asLeaderboardUiModel() })
                }
            } catch (e: Exception) {
                setState { copy(loading = false, error = e) }
            } finally {
                setState { copy(loading = false) }
            }
        }

    }
    private fun observeEvents() {
        viewModelScope.launch {
            events.collect{
                when (it) {
                    is LeaderboardUiEvent.ShareResult -> {
                        it.leaderboardPost.nickname = authStore.authData.value.nickname
                        postResult(
                            it.leaderboardPost
                        )
                    }
                    is LeaderboardUiEvent.AddResultLocally -> {
                        Log.d("LeaderboardViewModel", "observeEvents: ${it.leaderboardPost.result}")
                        val quizResult = QuizResult(
                            position = 0,
                            category = it.leaderboardPost.category,
                            result = it.leaderboardPost.result,
                            createdAt = System.currentTimeMillis()
                        )
                        updateUserResults(quizResult)
                    }
                }
            }
        }
    }
    private fun postResult(leaderboardPost: LeaderboardPost){
        val response = mutableStateOf(LeaderboardResponse(Result(0,"",0.0,0),0))
        viewModelScope.launch {
            try {
                response.value = repository.postResult(leaderboardPost)
                val quizResult = QuizResult(
                    position = response.value.ranking,
                    category = response.value.result.category,
                    result = response.value.result.result,
                    createdAt = response.value.result.createdAt
                )
                updateRank(quizResult)
            } catch (e: Exception) {
                throw e
            }
        }
    }
    private fun updateUserResults(quizResult: QuizResult){
        viewModelScope.launch {
            authStore.updateAuthData {
                Log.d("LeaderboardViewModel", "updateRank1: ")


                Log.d("LeaderboardViewModel", "updateRank2: ${this.bestRank} ${quizResult.position}")


                this.copy(
                    results = this.results + quizResult,
                    bestResult = if (this.bestResult.result < quizResult.result) quizResult else this.bestResult,
                    //bestResult = quizResult
                )

            }
            Log.d("LeaderboardViewModel", "updateRank3: ")


        }
    }
    private fun updateRank(quizResult: QuizResult){
        viewModelScope.launch {
            authStore.updateAuthData {
                val updatedResults =
                    this.results.mapIndexed { index, result ->
                        if (index == this.results.size - 1) {
                            result.copy(position = quizResult.position)
                        } else {
                            result
                        }
                    }

                this.copy(
                    results = updatedResults,
                    bestRank = if (this.bestRank > quizResult.position) quizResult.position else this.bestRank
                )
            }
        }
    }
    private fun Leaderboard.asLeaderboardUiModel() = LeaderboardUiModel(
        id = this.id,
        nickname = this.nickname,
        result = this.result,
        category = this.category,
        numberOfQuizzesPlayed = this.totalTimesPlayed
    )
}
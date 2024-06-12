package com.rma.catapult.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.catapult.cat.db.Cat
import com.rma.catapult.cat.list.api.CatListUiEvent
import com.rma.catapult.cat.list.api.model.CatListUiModel
import com.rma.catapult.drawer.AppDrawerContract
import com.rma.catapult.leaderboard.api.model.LeaderboardUiModel
import com.rma.catapult.leaderboard.model.Leaderboard
import com.rma.catapult.leaderboard.model.LeaderboardPost
import com.rma.catapult.leaderboard.repository.LeaderboardRepository
import com.rma.catapult.user.auth.AuthStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                // Handle the error here
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
                }
            }
        }
    }
    private fun postResult(leaderboardPost: LeaderboardPost) {
        viewModelScope.launch {
            try {
                repository.postResult(leaderboardPost)
            } catch (e: Exception) {
                throw e
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
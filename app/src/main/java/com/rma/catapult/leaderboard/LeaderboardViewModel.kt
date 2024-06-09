package com.rma.catapult.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.catapult.cat.db.Cat
import com.rma.catapult.cat.list.api.model.CatListUiModel
import com.rma.catapult.drawer.AppDrawerContract
import com.rma.catapult.leaderboard.api.model.LeaderboardUiModel
import com.rma.catapult.leaderboard.model.Leaderboard
import com.rma.catapult.leaderboard.repository.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository
): ViewModel() {

    private val _state = MutableStateFlow(LeaderboardState())
    val state = _state.asStateFlow()

    private fun setState(reducer: LeaderboardState.() -> LeaderboardState) {
        _state.update(reducer)
    }

    init {
        fetchResults()
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

    private fun Leaderboard.asLeaderboardUiModel() = LeaderboardUiModel(
        id = this.id,
        nickname = this.nickname,
        result = this.result,
        category = this.category,
        numberOfQuizzesPlayed = this.totalTimesPlayed
    )
}
package com.rma.catapult.user.profile


import androidx.lifecycle.ViewModel
import com.rma.catapult.user.auth.AuthStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val authStore: AuthStore
) : ViewModel() {

    private val _state = MutableStateFlow(UserProfileState())
    val state = _state.asStateFlow()

    private fun setState(reducer: UserProfileState.() -> UserProfileState) = _state.update(reducer)


    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        val authData = authStore.authData.value
        setState { copy(user = authData) }

    }



}
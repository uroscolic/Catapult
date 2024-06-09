package com.rma.catapult.user.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.catapult.user.auth.AuthStore
import com.rma.catapult.user.edit.UserEditContract.EditState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserEditViewModel @Inject constructor(
    private val authStore: AuthStore
) : ViewModel() {

    private val _state = MutableStateFlow(EditState())
    val state = _state.asStateFlow()
    private val events = MutableSharedFlow<UserEditUiEvent>()

    private fun setState(reducer: EditState.() -> EditState) = _state.update(reducer)

    fun setEvent(event: UserEditUiEvent){
        viewModelScope.launch {
            events.emit(event)
        }
    }
    init {
        getUserInfo()
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect{
                when (it) {
                    is UserEditUiEvent.UserEdited -> {
                        setState { copy(
                            name = it.user.name,
                            surname = it.user.surname,
                            email = it.user.email,
                            nickname = it.user.nickname
                        ) }
                        updateUser(it.user.name, it.user.surname, it.user.nickname, it.user.email)
                    }
                }
            }
        }
    }
    private fun getUserInfo() {
        val authData = authStore.authData.value
        if (authData.name.isNotEmpty() && authData.surname.isNotEmpty() && authData.nickname.isNotEmpty() && authData.email.isNotEmpty()) {
            setState { copy(name = authData.name, surname = authData.surname,
                email = authData.email, nickname = authData.nickname) }
        }
    }


    private fun updateUser(name: String, surname: String, nickname: String, email: String) {
        viewModelScope.launch {
            authStore.updateAuthData {
                this.copy(
                    name = name,
                    surname = surname,
                    nickname = nickname,
                    email = email
                )
            }
        }
    }

}
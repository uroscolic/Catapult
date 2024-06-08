package com.rma.catapult.user.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.catapult.cat.list.api.CatListUiEvent
import com.rma.catapult.drawer.AppDrawerContract
import com.rma.catapult.user.auth.AuthStore
import com.rma.catapult.user.register.UserRegisterContract.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserRegisterViewModel  @Inject constructor(
    private val authStore: AuthStore
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()
    private val events = MutableSharedFlow<UserRegisterUiEvent>()

    private fun setState(reducer: RegisterState.() -> RegisterState) = _state.update(reducer)

    fun setEvent(event: UserRegisterUiEvent){
        viewModelScope.launch {
            events.emit(event)
        }
    }
    init {
        checkIfUserIsRegistered()
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect{ it ->
                when (it) {
                    is UserRegisterUiEvent.UserRegistered -> {
                        setState { copy(registered = true,
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
    private fun emptyData(){
        viewModelScope.launch {
            authStore.updateAuthData {
                this.copy(
                    name = "",
                    surname = "",
                    nickname = "",
                    email = ""
                )
            }
        }
        setState { copy(registered = false, name = "", surname = "", email = "", nickname = "") }
    }
    private fun checkIfUserIsRegistered() {
        val authData = authStore.authData.value
        if (authData.name.isNotEmpty() && authData.surname.isNotEmpty() && authData.nickname.isNotEmpty() && authData.email.isNotEmpty()) {
            setState { copy(registered = true, name = authData.name, surname = authData.surname,
                email = authData.email, nickname = authData.nickname) }
        }
    }

    fun updateUser(name: String, surname: String, nickname: String, email: String) {
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
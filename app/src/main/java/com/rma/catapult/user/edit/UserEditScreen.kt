package com.rma.catapult.user.edit

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.util.PatternsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rma.catapult.cat.details.orange
import com.rma.catapult.core.compose.AppIconButton
import com.rma.catapult.core.theme.Samsung
import com.rma.catapult.user.edit.UserEditContract.EditState
import com.rma.catapult.user.model.User

fun NavGraphBuilder.editUser(
    route : String,
    onEditClick: () -> Unit,
    onClose: () -> Unit
) {

    composable(route){

        val userEditViewModel = hiltViewModel<UserEditViewModel>()
        val state = userEditViewModel.state.collectAsState()


        UserEditScreen(
            onEditClick = onEditClick,
            onClose = onClose,
            eventPublisher = { userEditViewModel.setEvent(it) },
            state = state.value
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEditScreen(
    onEditClick: () -> Unit,
    onClose: () -> Unit,
    eventPublisher: (UserEditUiEvent) -> Unit,
    state : EditState
) {
    var name by remember { mutableStateOf(state.name) }
    var surname by remember { mutableStateOf(state.surname) }
    var nickname by remember { mutableStateOf(state.nickname) }
    var email by remember { mutableStateOf(state.email) }
    val isNameValid = name.all { it.isLetter() }
    val isSurnameValid = surname.all { it.isLetter() }
    val isNicknameValid = nickname.all { it.isLetterOrDigit() || it == '_' }
    val isEmailValid = PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    val enabled = name.isNotEmpty() && isNameValid &&
            surname.isNotEmpty() && isSurnameValid &&
            nickname.isNotEmpty() && isNicknameValid &&
            email.isNotEmpty() && isEmailValid
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                },
                title = { Text("Edit Profile", fontFamily = Samsung, fontWeight = FontWeight.Medium) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name", fontFamily = Samsung) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Surname", fontFamily = Samsung) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = nickname,
                onValueChange = { nickname = it },
                label = { Text("Nickname", fontFamily = Samsung) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", fontFamily = Samsung) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    eventPublisher(
                        UserEditUiEvent.UserEdited(
                            User(
                                name = name,
                                surname = surname,
                                nickname = nickname,
                                email = email
                            )
                        )
                    )
                    onEditClick()
                },
                enabled = enabled,
                modifier = Modifier.width(200.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = orange
                )
            ) {
                Text("Save changes", fontFamily = Samsung)
            }
        }
    }
}
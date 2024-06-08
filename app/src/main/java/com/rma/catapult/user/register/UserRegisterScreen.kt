package com.rma.catapult.user.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.util.PatternsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rma.catapult.cat.details.CatDetailsScreen
import com.rma.catapult.cat.details.CatDetailsViewModel
import com.rma.catapult.cat.details.orange
import com.rma.catapult.core.theme.Samsung
import com.rma.catapult.user.model.User
import com.rma.catapult.user.register.UserRegisterContract.RegisterState


fun NavGraphBuilder.register(
    route : String,
    onRegisterClick: () -> Unit,
    alreadyRegistered: () -> Unit
) {

    composable(route){

        val userRegisterViewModel = hiltViewModel<UserRegisterViewModel>()
        val state = userRegisterViewModel.state.collectAsState()
        if(state.value.registered){
            alreadyRegistered()
        }
        UserRegisterScreen(
            onRegisterClick = onRegisterClick,
            eventPublisher = { userRegisterViewModel.setEvent(it) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRegisterScreen(
    onRegisterClick: () -> Unit,
    eventPublisher: (UserRegisterUiEvent) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
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
                title = { Text("Register", fontFamily = Samsung, fontWeight = FontWeight.Medium) }
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
                        UserRegisterUiEvent.UserRegistered(
                            User(
                                name = name,
                                surname = surname,
                                nickname = nickname,
                                email = email
                            )
                        )
                    )
                    onRegisterClick()
                },
                enabled = enabled,
                modifier = Modifier.width(200.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = orange
                )
            ) {
                Text("Register", fontFamily = Samsung)
            }
        }
    }
}
/*
@Preview
@Composable
fun UserRegisterScreenPreview() {
    UserRegisterScreen(onRegisterClick = {}, hiltViewModel())
}*/
package com.rma.catapult.user.profile


import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rma.catapult.cat.details.gold
import com.rma.catapult.core.compose.AppIconButton
import com.rma.catapult.core.theme.Samsung
import com.rma.catapult.user.model.QuizResult
import com.rma.catapult.user.model.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun NavGraphBuilder.profile(
    route : String,
    onEditClick: () -> Unit,
    onClose: () -> Unit
) {

    composable(route){

        val userProfileViewModel = hiltViewModel<UserProfileViewModel>()
        val state = userProfileViewModel.state.collectAsState()
        ProfileScreen(user = state.value.user,
            onEditClick = onEditClick,
            onClose = onClose
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(user: User, onEditClick: () -> Unit, onClose: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                },
                title = {
                    Text(
                        text = "Profile",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Samsung
                    )
                },
                actions = {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = gold
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "${user.name} ${user.surname} (${user.nickname})", fontSize = 18.sp, modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 25.dp))

            Text(text = user.email, fontSize = 18.sp, modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 25.dp))


            Text(
                text = "Best Rank: ${user.bestRank}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp, modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 25.dp)
            )

            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                thickness = 2.dp,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .shadow(2.dp, shape = MaterialTheme.shapes.medium)

            )

            Text(
                text = "Best Result",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            QuizResultItem(true,result = user.bestResult, bestRank = user.bestRank, bestResult = user.bestResult)


            Text(
                text = "Quiz Results",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(user.results.reversed()) { result ->
                    QuizResultItem(false, result, user.bestRank, user.bestResult)
                }
            }
        }
    }
}

@Composable
fun QuizResultItem(flag: Boolean , result: QuizResult, bestRank: Int, bestResult: QuizResult) {
    val isBestResult = result.result == bestResult.result
    val isBestRank = result.position == bestRank

    val borderColor = if (isBestResult || isBestRank) {
        Brush.linearGradient(
            colors = listOf(Color(0xFFFDD708), Color(0xFFD1B000))
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.Transparent)
        )
    }
    val resultText = if (isBestResult) {
        Color(0xFFD1B000)
    } else {
        Color.Unspecified
    }
    val rankText = if (isBestRank) {
        Color(0xFFD1B000)
    } else {
        Color.Unspecified
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        border = BorderStroke(
            width = 4.dp,
            brush = borderColor
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "Category: ${result.category}")
            Text(text = "Result: ${result.result}", color = resultText)
            if (result.position != 0)
                Text(text = "Position: #${result.position}", color = rankText)
            else {
                if(!flag)
                    Text(text = "Position: Not Ranked")
            }
            Text(text = "Date: ${formatDate(result.createdAt)}")
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}
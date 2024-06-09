package com.rma.catapult.leaderboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rma.catapult.core.theme.Samsung
import com.rma.catapult.leaderboard.api.model.LeaderboardUiModel


fun NavGraphBuilder.leaderboard(route: String) {
    composable(route) {
        val viewModel: LeaderboardViewModel = hiltViewModel<LeaderboardViewModel>()
        val state by viewModel.state.collectAsState()
        LeaderboardScreen(state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(

    state: LeaderboardState
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Leaderboard", fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = Samsung) }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                state.loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                state.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${state.error.message}")
                    }
                }
                state.results.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No leaderboard data available.")
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(state.results) { result ->
                            key(result.id) {
                                LeaderboardItem(
                                    result
                                )
                                Spacer(modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardItem(entry: LeaderboardUiModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val Bronze = Color(0xFFCD7F32)
            val color = when (entry.id) {
                1 -> Color.Yellow
                2 -> Color.Gray
                3 -> Bronze
                else -> Color.Transparent
            }

            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Trophy",
                tint = color,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = entry.nickname, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = "Score: ${entry.result}", fontSize = 16.sp)
                Text(text = "Position: ${entry.id}", fontSize = 16.sp)
                Text(text = "Total Appearances: ${entry.numberOfQuizzesPlayed}", fontSize = 16.sp)
            }
        }
    }
}
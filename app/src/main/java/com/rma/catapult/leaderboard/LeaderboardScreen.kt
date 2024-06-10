package com.rma.catapult.leaderboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rma.catapult.core.compose.AppIconButton
import com.rma.catapult.core.theme.Samsung
import com.rma.catapult.leaderboard.api.model.LeaderboardUiModel


fun NavGraphBuilder.leaderboard(
    route: String,
    onClose: () -> Unit
) {
    composable(route) {
        val viewModel: LeaderboardViewModel = hiltViewModel<LeaderboardViewModel>()
        val state by viewModel.state.collectAsState()
        LeaderboardScreen(state, onClose)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    state: LeaderboardState,
    onClose: () -> Unit,
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                },
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

    val BronzeStart = Color(0xFFCD7F32) // Bronze color
    val BronzeEnd = Color(0xFF8B4513) // Darker Bronze color

    val GoldStart = Color(0xFFD1B000) // Gold color
    val GoldEnd = Color(0xFFFFD700) // Lighter Gold color

    val SilverStart = Color(0xFFC0C0C0) // Silver color
    val SilverEnd = Color(0xFFA9A9A9) // Darker Silver color

    val colorGradient = when (entry.id) {
        1 -> listOf(GoldStart, GoldEnd)
        2 -> listOf(SilverStart, SilverEnd)
        3 -> listOf(BronzeStart, BronzeEnd)
        else -> listOf(Color.Transparent, Color.Transparent)
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(
            2.dp,
            brush = Brush.linearGradient(
                colors = colorGradient
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Trophy",
                tint = colorGradient.first(),
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
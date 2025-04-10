package com.rma.catapult.quiz.questions

import android.os.CountDownTimer
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage
import com.rma.catapult.cat.details.gold
import com.rma.catapult.core.theme.Samsung
import com.rma.catapult.leaderboard.LeaderboardUiEvent
import com.rma.catapult.leaderboard.LeaderboardViewModel
import com.rma.catapult.leaderboard.model.LeaderboardPost
import com.rma.catapult.quiz.questions.model.QuestionUiModel
import kotlinx.coroutines.delay
import kotlin.random.Random

fun NavGraphBuilder.questions(
    route: String,
    toCatList: () -> Unit
) = composable(
    route = route,
) {
    val questionsViewModel = hiltViewModel<QuestionsViewModel>()
    val leaderboardViewModel = hiltViewModel<LeaderboardViewModel>()

    val state = questionsViewModel.state.collectAsState()
    QuestionScreen(state.value,
        toCatList = toCatList,
        eventPublisher = { event ->
            leaderboardViewModel.setEvent(event)
        }
    )
}

@Composable
fun QuestionScreen(
    state: QuestionsState,
    toCatList: () -> Unit,
    eventPublisher: (LeaderboardUiEvent) -> Unit
) {
    var score by remember { mutableIntStateOf(0) }
    var timesUp by remember { mutableStateOf(false) }
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var showQuestion by remember { mutableStateOf(true) }

    BackHandler {}

    if (state.questions.isNotEmpty()) {
        var timeLeft by remember { mutableIntStateOf(300) }

        val timer = remember {
            object : CountDownTimer((timeLeft * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeft = (millisUntilFinished / 1000).toInt()
                }

                override fun onFinish() {
                    timesUp = true
                }
            }.start()
        }

        if (currentQuestionIndex >= state.questions.size || timesUp) {
            timer.cancel()
            EndOfQuizScreen(
                score = score,
                onHomeClick = toCatList,
                timeLeft = timeLeft,
                eventPublisher = eventPublisher
            )
        } else {
            LaunchedEffect(currentQuestionIndex) {
                showQuestion = false
                delay(200)
                showQuestion = true
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AnimatedVisibility(
                    visible = showQuestion,
                    enter = fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300)
                    ),
                    exit = fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(300)
                    ),
                ) {
                    key(currentQuestionIndex) {
                        QuestionContent(
                            question = state.questions[currentQuestionIndex],
                            onAnswerSelected = {
                                if (it) {
                                    score += 1
                                }
                                currentQuestionIndex += 1
                            },
                            number = currentQuestionIndex + 1,
                            onGiveUp = toCatList,
                            score = score,
                            timeLeft = timeLeft,
                        )
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndOfQuizScreen(
    score: Int,
    onHomeClick: () -> Unit,
    timeLeft: Int,
    eventPublisher: (LeaderboardUiEvent) -> Unit
) {
    var points = score * 2.5 * (1 + (timeLeft + 120.0) / 300.0)
    if (points > 100.0)
        points = 100.0
    Log.d("EndOfQuizScreen", "EndOfQuizScreen: $points")
    points = (points * 100).toInt() / 100.0

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("End of Quiz") },
            )
        },
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = if (timeLeft == 0) "Time's Up!" else "Quiz Completed",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Your Score: $points/100",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))
            var flag by remember {
                mutableStateOf(false)
            }
            if (!flag) {
                flag = true
                eventPublisher(
                    LeaderboardUiEvent.AddResultLocally(
                        LeaderboardPost(
                            result = points,
                            category = 3
                        )
                    )
                )
            }
            Button(
                onClick = {
                    eventPublisher(LeaderboardUiEvent.ShareResult(LeaderboardPost(result = points, category = 3)))
                    onHomeClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = gold
                )
            ) {
                Text(
                    "Share Result",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onHomeClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = gold
                )
            ) {
                Text(
                    "Home",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = if (points < 40) "Better luck next time!"
                else if (points >= 40 && points < 75) "Nice!" else "Wow, you are a genius!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionContent(
    question: QuestionUiModel,
    onAnswerSelected: (Boolean) -> Unit,
    number: Int,
    onGiveUp: () -> Unit,
    score: Int,
    timeLeft: Int,
) {
    var showDialog by remember { mutableStateOf(false) }
    val correctOption by remember(key1 = number) { mutableStateOf(Random.nextBoolean()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Question #${number}", fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = Samsung) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "Correct Answers: ${score}/20",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${timeLeft / 60}:${String.format("%02d", timeLeft % 60)}",
                        fontWeight = FontWeight.Bold,
                        fontFamily = Samsung,
                        fontSize = 18.sp,
                        color = if (timeLeft < 60) Color(0xFFD30000)
                        else Color.Unspecified
                    )

                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = question.text, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                SubcomposeAsyncImage(
                    model = if(correctOption) question.correctAnswer else question.incorrectAnswer,
                    contentDescription = "option1",
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onAnswerSelected(correctOption) },
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(36.dp),
                            )
                        }
                    }
                )

                SubcomposeAsyncImage(
                    model = if(correctOption) question.incorrectAnswer else question.correctAnswer,
                    contentDescription = "option2",
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onAnswerSelected(correctOption) },
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(36.dp),
                            )
                        }
                    }
                )
                Button(
                    onClick = { showDialog = true },
                    content = {
                        Text("Give up", fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Samsung
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = gold
                    )
                )
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = {
                            Text(text = "Confirm Give Up",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = Samsung
                            ) },
                        text = {
                            Text(text = "Are you sure you want to give up?",
                                fontFamily = Samsung

                            ) },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showDialog = false
                                    onGiveUp()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = gold
                                )
                            ) {
                                Text(text = "Yes",
                                    fontFamily = Samsung
                                )
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showDialog = false },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = gold
                                )
                            ) {
                                Text(text = "No",
                                    fontFamily = Samsung
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
